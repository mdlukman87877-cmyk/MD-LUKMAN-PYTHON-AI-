package com.example.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

data class PythonResult(
    val output: String,
    val error: String?,
    val executionTimeMs: Long,
    val isSuccess: Boolean
)

class PythonExecutionEngine(private val context: Context) {

    suspend fun execute(code: String, input: String = ""): PythonResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        val trimmed = code.trim()

        if (trimmed.isEmpty()) {
            return@withContext PythonResult(
                output = "",
                error = "Code editor is empty. Kuch Python code likhiye!",
                executionTimeMs = 0,
                isSuccess = false
            )
        }

        // Run in safe sandboxed Kotlin-Python interpreter
        try {
            val result = withTimeoutOrNull(5000L) {
                runInterpretedPython(code, input)
            }

            val elapsed = System.currentTimeMillis() - startTime
            if (result == null) {
                return@withContext PythonResult(
                    output = "",
                    error = "TimeoutError: Code execution exceeded 5 seconds limit (Infinite loop detect hua!).",
                    executionTimeMs = elapsed,
                    isSuccess = false
                )
            }

            return@withContext result.copy(executionTimeMs = elapsed)
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startTime
            return@withContext PythonResult(
                output = "",
                error = "${e.javaClass.simpleName}: ${e.message}",
                executionTimeMs = elapsed,
                isSuccess = false
            )
        }
    }

    private fun runInterpretedPython(code: String, standardInput: String): PythonResult {
        val outputBuffer = StringBuilder()
        val variables = mutableMapOf<String, Any?>()
        val customFunctions = mutableMapOf<String, Pair<List<String>, List<String>>>()

        val inputLines = standardInput.lines().filter { it.isNotBlank() }.toMutableList()
        val rawLines = code.lines()

        var currentLineNum = 0
        try {
            var i = 0
            while (i < rawLines.size) {
                currentLineNum = i + 1
                val line = rawLines[i]
                val trimmedLine = line.trim()

                // Skip comments and blanks
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    i++
                    continue
                }

                // Handle def function
                if (trimmedLine.startsWith("def ")) {
                    val defRest = trimmedLine.removePrefix("def ").trim()
                    val funcName = defRest.substringBefore("(").trim()
                    val paramString = defRest.substringAfter("(").substringBefore(")").trim()
                    val params = if (paramString.isEmpty()) emptyList() else paramString.split(",").map { it.trim() }

                    // Collect indented body
                    val body = mutableListOf<String>()
                    i++
                    while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                        if (rawLines[i].isNotBlank()) {
                            body.add(rawLines[i].trim())
                        }
                        i++
                    }
                    customFunctions[funcName] = Pair(params, body)
                    continue
                }

                // Handle for loop: for i in range(n): or for item in list:
                if (trimmedLine.startsWith("for ") && trimmedLine.endsWith(":")) {
                    val forPart = trimmedLine.removePrefix("for ").removeSuffix(":").trim()
                    val loopVar = forPart.substringBefore(" in ").trim()
                    val iterableExpr = forPart.substringAfter(" in ").trim()

                    // Collect indented block
                    val loopBody = mutableListOf<String>()
                    i++
                    while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                        if (rawLines[i].isNotBlank()) {
                            loopBody.add(rawLines[i].trim())
                        }
                        i++
                    }

                    val items = evaluateIterable(iterableExpr, variables)
                    for (item in items) {
                        variables[loopVar] = item
                        for (bodyLine in loopBody) {
                            executeSingleLine(bodyLine, variables, outputBuffer, inputLines, customFunctions)
                        }
                    }
                    continue
                }

                // Handle while loop: while cond:
                if (trimmedLine.startsWith("while ") && trimmedLine.endsWith(":")) {
                    val conditionExpr = trimmedLine.removePrefix("while ").removeSuffix(":").trim()
                    val loopBody = mutableListOf<String>()
                    i++
                    while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                        if (rawLines[i].isNotBlank()) {
                            loopBody.add(rawLines[i].trim())
                        }
                        i++
                    }

                    var iterations = 0
                    while (evaluateCondition(conditionExpr, variables) && iterations < 1000) {
                        iterations++
                        for (bodyLine in loopBody) {
                            executeSingleLine(bodyLine, variables, outputBuffer, inputLines, customFunctions)
                        }
                    }
                    if (iterations >= 1000) {
                        throw RuntimeException("Infinite loop detected in while statement!")
                    }
                    continue
                }

                // Handle if / elif / else block
                if (trimmedLine.startsWith("if ") && trimmedLine.endsWith(":")) {
                    val cond = trimmedLine.removePrefix("if ").removeSuffix(":").trim()
                    val ifBody = mutableListOf<String>()
                    i++
                    while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                        if (rawLines[i].isNotBlank()) ifBody.add(rawLines[i].trim())
                        i++
                    }

                    var conditionMet = evaluateCondition(cond, variables)
                    if (conditionMet) {
                        for (bodyLine in ifBody) {
                            executeSingleLine(bodyLine, variables, outputBuffer, inputLines, customFunctions)
                        }
                    }

                    // Check for subsequent elif or else
                    while (i < rawLines.size && (rawLines[i].trim().startsWith("elif ") || rawLines[i].trim().startsWith("else:"))) {
                        val nextTrimmed = rawLines[i].trim()
                        if (nextTrimmed.startsWith("elif ")) {
                            val elifCond = nextTrimmed.removePrefix("elif ").removeSuffix(":").trim()
                            val elifBody = mutableListOf<String>()
                            i++
                            while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                                if (rawLines[i].isNotBlank()) elifBody.add(rawLines[i].trim())
                                i++
                            }
                            if (!conditionMet && evaluateCondition(elifCond, variables)) {
                                conditionMet = true
                                for (bodyLine in elifBody) {
                                    executeSingleLine(bodyLine, variables, outputBuffer, inputLines, customFunctions)
                                }
                            }
                        } else if (nextTrimmed.startsWith("else:")) {
                            val elseBody = mutableListOf<String>()
                            i++
                            while (i < rawLines.size && (rawLines[i].startsWith("    ") || rawLines[i].startsWith("\t") || rawLines[i].isBlank())) {
                                if (rawLines[i].isNotBlank()) elseBody.add(rawLines[i].trim())
                                i++
                            }
                            if (!conditionMet) {
                                conditionMet = true
                                for (bodyLine in elseBody) {
                                    executeSingleLine(bodyLine, variables, outputBuffer, inputLines, customFunctions)
                                }
                            }
                        }
                    }
                    continue
                }

                // Standard single statement
                executeSingleLine(trimmedLine, variables, outputBuffer, inputLines, customFunctions)
                i++
            }

            return PythonResult(
                output = outputBuffer.toString().ifEmpty { "Program executed successfully with no output." },
                error = null,
                executionTimeMs = 0,
                isSuccess = true
            )
        } catch (e: Exception) {
            val errorMsg = formatPythonError(e, currentLineNum)
            return PythonResult(
                output = outputBuffer.toString(),
                error = errorMsg,
                executionTimeMs = 0,
                isSuccess = false
            )
        }
    }

    private fun executeSingleLine(
        line: String,
        variables: MutableMap<String, Any?>,
        outputBuffer: StringBuilder,
        inputLines: MutableList<String>,
        customFunctions: Map<String, Pair<List<String>, List<String>>>
    ) {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return

        // 1. print(...) statement
        if (trimmed.startsWith("print(") && trimmed.endsWith(")")) {
            val inner = trimmed.substring(6, trimmed.length - 1).trim()
            val text = evaluatePrintArguments(inner, variables)
            outputBuffer.append(text).append("\n")
            return
        }

        // 2. Variable assignment: x = ... or x += ...
        if (trimmed.contains("+=") || trimmed.contains("-=") || trimmed.contains("*=") || trimmed.contains("=")) {
            handleAssignment(trimmed, variables, inputLines, customFunctions)
            return
        }

        // 3. List operations like list.append(...)
        if (trimmed.contains(".append(")) {
            val varName = trimmed.substringBefore(".append(").trim()
            val arg = trimmed.substringAfter(".append(").removeSuffix(")").trim()
            val list = variables[varName] as? MutableList<Any?>
            if (list != null) {
                list.add(evaluateExpression(arg, variables))
            } else {
                throw RuntimeException("NameError: name '$varName' is not defined as a list")
            }
            return
        }

        // 4. Function call like greet("Lukman")
        if (trimmed.contains("(") && trimmed.endsWith(")")) {
            val funcName = trimmed.substringBefore("(").trim()
            val func = customFunctions[funcName]
            if (func != null) {
                val argStr = trimmed.substringAfter("(").removeSuffix(")").trim()
                val args = if (argStr.isEmpty()) emptyList() else splitArgs(argStr).map { evaluateExpression(it, variables) }
                val localVars = HashMap(variables)
                for (idx in func.first.indices) {
                    if (idx < args.size) {
                        localVars[func.first[idx]] = args[idx]
                    }
                }
                for (bodyLine in func.second) {
                    executeSingleLine(bodyLine, localVars, outputBuffer, inputLines, customFunctions)
                }
                return
            }
        }
    }

    private fun handleAssignment(
        line: String,
        variables: MutableMap<String, Any?>,
        inputLines: MutableList<String>,
        customFunctions: Map<String, Pair<List<String>, List<String>>>
    ) {
        when {
            line.contains("+=") -> {
                val varName = line.substringBefore("+=").trim()
                val expr = line.substringAfter("+=").trim()
                val curVal = variables[varName] ?: 0
                val addVal = evaluateExpression(expr, variables)
                variables[varName] = addValues(curVal, addVal)
            }
            line.contains("-=") -> {
                val varName = line.substringBefore("-=").trim()
                val expr = line.substringAfter("-=").trim()
                val curVal = (variables[varName] as? Number)?.toDouble() ?: 0.0
                val subVal = (evaluateExpression(expr, variables) as? Number)?.toDouble() ?: 0.0
                variables[varName] = formatNumber(curVal - subVal)
            }
            line.contains("*=") -> {
                val varName = line.substringBefore("*=").trim()
                val expr = line.substringAfter("*=").trim()
                val curVal = (variables[varName] as? Number)?.toDouble() ?: 1.0
                val multVal = (evaluateExpression(expr, variables) as? Number)?.toDouble() ?: 1.0
                variables[varName] = formatNumber(curVal * multVal)
            }
            line.contains("=") -> {
                val varName = line.substringBefore("=").trim()
                val expr = line.substringAfter("=").trim()

                // Check for input() or int(input())
                if (expr.contains("input(")) {
                    val rawInput = if (inputLines.isNotEmpty()) inputLines.removeAt(0) else "0"
                    if (expr.startsWith("int(")) {
                        variables[varName] = rawInput.trim().toIntOrNull() ?: 0
                    } else if (expr.startsWith("float(")) {
                        variables[varName] = rawInput.trim().toDoubleOrNull() ?: 0.0
                    } else {
                        variables[varName] = rawInput
                    }
                    return
                }

                // Check for list comprehension or list literal
                if (expr.startsWith("[") && expr.endsWith("]")) {
                    val inner = expr.substring(1, expr.length - 1).trim()
                    if (inner.isEmpty()) {
                        variables[varName] = mutableListOf<Any?>()
                    } else {
                        val elements = splitArgs(inner).map { evaluateExpression(it, variables) }
                        variables[varName] = elements.toMutableList()
                    }
                    return
                }

                val value = evaluateExpression(expr, variables)
                variables[varName] = value
            }
        }
    }

    private fun evaluatePrintArguments(argsString: String, variables: Map<String, Any?>): String {
        if (argsString.isEmpty()) return ""
        val args = splitArgs(argsString)
        val sb = StringBuilder()
        for (i in args.indices) {
            val evaluated = evaluateExpression(args[i].trim(), variables)
            sb.append(evaluated.toString())
            if (i < args.size - 1) sb.append(" ")
        }
        return sb.toString()
    }

    private fun evaluateExpression(expr: String, variables: Map<String, Any?>): Any? {
        val trimmed = expr.trim()

        // f-string: f"Hello {name}"
        if (trimmed.startsWith("f\"") || trimmed.startsWith("f'")) {
            val raw = trimmed.substring(2, trimmed.length - 1)
            var result = raw
            val regex = "\\{([^}]+)\\}".toRegex()
            regex.findAll(raw).forEach { match ->
                val varExpr = match.groupValues[1]
                val eval = evaluateExpression(varExpr, variables)
                result = result.replace(match.value, eval.toString())
            }
            return result
        }

        // String literals: "text" or 'text'
        if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) ||
            (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            return trimmed.substring(1, trimmed.length - 1)
        }

        // Boolean literals
        if (trimmed == "True") return true
        if (trimmed == "False") return false
        if (trimmed == "None") return null

        // Number literals
        trimmed.toIntOrNull()?.let { return it }
        trimmed.toDoubleOrNull()?.let { return it }

        // len(x)
        if (trimmed.startsWith("len(") && trimmed.endsWith(")")) {
            val target = trimmed.substring(4, trimmed.length - 1).trim()
            val obj = evaluateExpression(target, variables)
            return when (obj) {
                is String -> obj.length
                is List<*> -> obj.size
                is Map<*, *> -> obj.size
                else -> 0
            }
        }

        // str(), int(), float()
        if (trimmed.startsWith("str(") && trimmed.endsWith(")")) {
            val inner = trimmed.substring(4, trimmed.length - 1).trim()
            return evaluateExpression(inner, variables).toString()
        }
        if (trimmed.startsWith("int(") && trimmed.endsWith(")")) {
            val inner = trimmed.substring(4, trimmed.length - 1).trim()
            val v = evaluateExpression(inner, variables)
            return when (v) {
                is Number -> v.toInt()
                is String -> v.toIntOrNull() ?: 0
                else -> 0
            }
        }

        // String methods: .upper(), .lower(), .replace()
        if (trimmed.contains(".upper()")) {
            val target = trimmed.substringBefore(".upper()").trim()
            return evaluateExpression(target, variables).toString().uppercase()
        }
        if (trimmed.contains(".lower()")) {
            val target = trimmed.substringBefore(".lower()").trim()
            return evaluateExpression(target, variables).toString().lowercase()
        }

        // Arithmetic binary operations: a + b, a - b, a * b, a / b, a % b, a // b
        if (trimmed.contains("+") && !trimmed.startsWith("+")) {
            val parts = trimmed.split("+", limit = 2)
            val left = evaluateExpression(parts[0], variables)
            val right = evaluateExpression(parts[1], variables)
            return addValues(left, right)
        }
        if (trimmed.contains("-") && !trimmed.startsWith("-")) {
            val parts = trimmed.split("-", limit = 2)
            val left = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
            val right = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 0.0
            return formatNumber(left - right)
        }
        if (trimmed.contains("*")) {
            val parts = trimmed.split("*", limit = 2)
            val left = evaluateExpression(parts[0], variables)
            val right = evaluateExpression(parts[1], variables)
            if (left is String && right is Number) {
                return left.repeat(right.toInt().coerceAtLeast(0))
            }
            val numLeft = (left as? Number)?.toDouble() ?: 0.0
            val numRight = (right as? Number)?.toDouble() ?: 0.0
            return formatNumber(numLeft * numRight)
        }
        if (trimmed.contains("//")) {
            val parts = trimmed.split("//", limit = 2)
            val left = (evaluateExpression(parts[0], variables) as? Number)?.toInt() ?: 0
            val right = (evaluateExpression(parts[1], variables) as? Number)?.toInt() ?: 1
            if (right == 0) throw ArithmeticException("ZeroDivisionError: integer division or modulo by zero")
            return left / right
        }
        if (trimmed.contains("/")) {
            val parts = trimmed.split("/", limit = 2)
            val left = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
            val right = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 1.0
            if (right == 0.0) throw ArithmeticException("ZeroDivisionError: division by zero")
            return formatNumber(left / right)
        }
        if (trimmed.contains("%")) {
            val parts = trimmed.split("%", limit = 2)
            val left = (evaluateExpression(parts[0], variables) as? Number)?.toInt() ?: 0
            val right = (evaluateExpression(parts[1], variables) as? Number)?.toInt() ?: 1
            if (right == 0) throw ArithmeticException("ZeroDivisionError: integer modulo by zero")
            return left % right
        }

        // List indexing: list[0]
        if (trimmed.contains("[") && trimmed.endsWith("]")) {
            val listVar = trimmed.substringBefore("[").trim()
            val indexExpr = trimmed.substringAfter("[").removeSuffix("]").trim()
            val list = variables[listVar] as? List<*>
            val idx = (evaluateExpression(indexExpr, variables) as? Number)?.toInt() ?: 0
            if (list != null) {
                if (idx < 0 || idx >= list.size) throw IndexOutOfBoundsException("IndexError: list index out of range")
                return list[idx]
            }
        }

        // Variable lookup
        if (variables.containsKey(trimmed)) {
            return variables[trimmed]
        }

        // If identifier not found and not a literal
        if (trimmed.matches("^[a-zA-Z_][a-zA-Z0-9_]*$".toRegex())) {
            throw RuntimeException("NameError: name '$trimmed' is not defined")
        }

        return trimmed
    }

    private fun evaluateCondition(expr: String, variables: Map<String, Any?>): Boolean {
        val trimmed = expr.trim()
        when {
            trimmed.contains("==") -> {
                val parts = trimmed.split("==", limit = 2)
                return evaluateExpression(parts[0], variables)?.toString() == evaluateExpression(parts[1], variables)?.toString()
            }
            trimmed.contains("!=") -> {
                val parts = trimmed.split("!=", limit = 2)
                return evaluateExpression(parts[0], variables)?.toString() != evaluateExpression(parts[1], variables)?.toString()
            }
            trimmed.contains("<=") -> {
                val parts = trimmed.split("<=", limit = 2)
                val l = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
                val r = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 0.0
                return l <= r
            }
            trimmed.contains(">=") -> {
                val parts = trimmed.split(">=", limit = 2)
                val l = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
                val r = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 0.0
                return l >= r
            }
            trimmed.contains("<") -> {
                val parts = trimmed.split("<", limit = 2)
                val l = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
                val r = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 0.0
                return l < r
            }
            trimmed.contains(">") -> {
                val parts = trimmed.split(">", limit = 2)
                val l = (evaluateExpression(parts[0], variables) as? Number)?.toDouble() ?: 0.0
                val r = (evaluateExpression(parts[1], variables) as? Number)?.toDouble() ?: 0.0
                return l > r
            }
            trimmed.contains(" in ") -> {
                val elem = evaluateExpression(trimmed.substringBefore(" in "), variables)
                val container = evaluateExpression(trimmed.substringAfter(" in "), variables)
                if (container is List<*>) return container.contains(elem)
                if (container is String) return container.contains(elem.toString())
                return false
            }
            else -> {
                val v = evaluateExpression(trimmed, variables)
                return when (v) {
                    is Boolean -> v
                    is Number -> v.toDouble() != 0.0
                    is String -> v.isNotEmpty()
                    null -> false
                    else -> true
                }
            }
        }
    }

    private fun evaluateIterable(expr: String, variables: Map<String, Any?>): List<Any?> {
        val trimmed = expr.trim()
        if (trimmed.startsWith("range(") && trimmed.endsWith(")")) {
            val inner = trimmed.substring(6, trimmed.length - 1).trim()
            val args = splitArgs(inner).map { (evaluateExpression(it, variables) as? Number)?.toInt() ?: 0 }
            return when (args.size) {
                1 -> (0 until args[0]).toList()
                2 -> (args[0] until args[1]).toList()
                3 -> {
                    val step = if (args[2] == 0) 1 else args[2]
                    (args[0] until args[1] step step).toList()
                }
                else -> emptyList()
            }
        }
        val target = evaluateExpression(trimmed, variables)
        if (target is List<*>) return target
        if (target is String) return target.map { it.toString() }
        return emptyList()
    }

    private fun addValues(a: Any?, b: Any?): Any {
        if (a is String || b is String) return a.toString() + b.toString()
        if (a is List<*> && b is List<*>) {
            val list = mutableListOf<Any?>()
            list.addAll(a)
            list.addAll(b)
            return list
        }
        val numA = (a as? Number)?.toDouble() ?: 0.0
        val numB = (b as? Number)?.toDouble() ?: 0.0
        return formatNumber(numA + numB)
    }

    private fun formatNumber(d: Double): Any {
        return if (d == d.toLong().toDouble()) d.toLong() else d
    }

    private fun splitArgs(args: String): List<String> {
        val result = mutableListOf<String>()
        var depth = 0
        var inQuotes = false
        var quoteChar = ' '
        var current = StringBuilder()

        for (ch in args) {
            if ((ch == '"' || ch == '\'') && !inQuotes) {
                inQuotes = true
                quoteChar = ch
                current.append(ch)
            } else if (ch == quoteChar && inQuotes) {
                inQuotes = false
                current.append(ch)
            } else if (!inQuotes && (ch == '(' || ch == '[' || ch == '{')) {
                depth++
                current.append(ch)
            } else if (!inQuotes && (ch == ')' || ch == ']' || ch == '}')) {
                depth--
                current.append(ch)
            } else if (ch == ',' && depth == 0 && !inQuotes) {
                result.add(current.toString().trim())
                current = StringBuilder()
            } else {
                current.append(ch)
            }
        }
        if (current.isNotBlank()) result.add(current.toString().trim())
        return result
    }

    private fun formatPythonError(e: Exception, lineNum: Int): String {
        val type = e.javaClass.simpleName.replace("RuntimeException", "Error")
        val msg = e.message ?: "Unknown error"
        return """
            Traceback (most recent call last):
              File "<stdin>", line $lineNum, in <module>
            $type: $msg
        """.trimIndent()
    }
}
