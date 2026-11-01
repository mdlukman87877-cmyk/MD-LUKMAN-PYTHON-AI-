package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiTutorService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private val systemInstruction = """
        Aap ho 'MD LUKMAN PYTHON AI' - Ek expert, friendly aur patient Python Teacher, jise MD Lukman ne banaya hai.
        Aapka maksad students ko Python programming bilkul zero level se advanced level tak simple Hindi + English (Hinglish) mein sikhana hai.
        Rules:
        1. Explanations simple aur practical Hindi mein hone chahiye taaki koi bhi beginner asani se samajh sake.
        2. Technical terms aur programming syntax English mein hi likhein (jaise variables, loop, function, list, dictionary, indentation).
        3. Code ka line-by-line explanation dein.
        4. Har concept ke sath real-life daily examples (e.g., tea stall, grocery bag, bank ATM) zaroor dein.
        5. Agar student error puche ya code de, toh:
           - Error ko Hindi mein samjhao.
           - Corrected code provide karo.
           - Explain karo ki mistake kya thi aur future mein kaise avoid karein.
        6. Tone hamesha supportive, encouraging aur patient hona chahiye ("Chinta mat kijiye, main aapko step by step sikhata hoon").
    """.trimIndent()

    suspend fun askTutor(userQuestion: String, codeContext: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val hasValidKey = apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY"

        if (hasValidKey) {
            try {
                val response = callGeminiApi(apiKey, userQuestion, codeContext)
                if (response.isNotBlank()) {
                    return@withContext response
                }
            } catch (e: Exception) {
                // Network or API failure -> Fall back gracefully to built-in AI tutor
            }
        }

        // Built-in intelligent educational tutor response
        return@withContext generateLocalTutorResponse(userQuestion, codeContext)
    }

    private fun callGeminiApi(apiKey: String, question: String, codeContext: String?): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val promptText = buildString {
            append(systemInstruction)
            append("\n\nStudent ka prashn / Doubt: ")
            append(question)
            if (!codeContext.isNullOrBlank()) {
                append("\n\nStudent ka Python Code / Error context:\n```python\n")
                append(codeContext)
                append("\n```")
            }
        }

        val jsonBody = JSONObject().apply {
            val contents = JSONArray().apply {
                val partObj = JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", promptText)
                        })
                    })
                }
                put(partObj)
            }
            put("contents", contents)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("HTTP ${response.code}: ${response.message}")
            }
            val bodyString = response.body?.string() ?: throw Exception("Empty response")
            val root = JSONObject(bodyString)
            val candidates = root.getJSONArray("candidates")
            if (candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text")
                }
            }
        }
        return ""
    }

    private fun generateLocalTutorResponse(question: String, codeContext: String?): String {
        val q = question.lowercase()

        if (q.contains("error") || codeContext?.contains("Error") == true) {
            return """
                🤖 **AI Error Solver (MD LUKMAN PYTHON AI):**

                नमस्ते! आपके कोड में आई एरर को समझ लेते हैं:

                🔍 **Mistake क्या है?**
                Python में Indentation (स्पेस) और Syntax का बहुत ध्यान रखना पड़ता है। अक्सर हम `:` भूल जाते हैं या गलत स्पेस दे देते हैं।

                💡 **समाधान (Fix):**
                1. हर `if`, `for`, `while`, या `def` के अंत में colon (`:`) ज़रूर लगाएं।
                2. ब्लॉक के अंदर के कोड को 4 spaces (Tab) इंडेंट करें।
                3. Variable नाम में स्पेस न दें (जैसे `my_var` लिखें, `my var` नहीं)।

                ✅ **Example Correct Code:**
                ```python
                age = 18
                if age >= 18:
                    print("Aap voting kar sakte hain!")
                else:
                    print("Abhi aap eligible nahi hain.")
                ```

                यदि आपका कोई विशेष एरर है, तो कोड एडिटर में Run करके मुझे दिखाएं, मैं तुरंत लाइन-बाय-लाइन समझाऊंगा!
            """.trimIndent()
        }

        if (q.contains("variable") || q.contains("kya hota hai") || q.contains("what is")) {
            return """
                🤖 **MD Lukman AI Tutor:**

                बहुत ही बढ़िया सवाल! आइए **Variables** को एक आसान उदाहरण से समझते हैं:

                📦 **Real-Life Example:**
                सोचिए आपके किचन में अलग-अलग डिब्बे (containers) हैं। एक डिब्बे में आपने चीनी रखी, दूसरे में नमक।
                Python में **Variable** बिल्कुल उसी डिब्बे (Container) की तरह होता है, जो कंप्यूटर की मेमोरी में किसी Value (Data) को स्टोर करता है।

                💻 **Python Code Example:**
                ```python
                name = "Rahul"     # String (Text store kiya)
                age = 20           # Integer (Number store kiya)
                height = 5.8       # Float (Decimal number)
                is_student = True  # Boolean (True ya False)
                ```

                🔍 **Line-by-Line Explanation:**
                - `name = "Rahul"`: यहाँ `name` डिब्बा है और `"Rahul"` उसके अंदर रखा सामान।
                - `=` को 'Assignment Operator' कहते हैं, जो दाहिनी ओर की वैल्यू को बाईं ओर के वेरिएबल में डालता है।

                💡 **Tip:** Variable का नाम हमेशा meaningful रखें, ताकि कोड पढ़ने में आसानी हो!
            """.trimIndent()
        }

        if (q.contains("loop") || q.contains("for") || q.contains("while")) {
            return """
                🤖 **MD Lukman AI Tutor:**

                Loops का सीधा मतलब है: **एक ही काम को बार-बार दोहराना (Repeat करना)!**

                🔄 **Real-Life Example:**
                मान लीजिए आपको अपने 5 दोस्तों को 'Happy Birthday' विश करना है। या तो आप 5 बार अलग-अलग मैसेज टाइप करें, या फिर एक लूप चला दें जो 1 से 5 तक अपने आप मैसेज भेज दे!

                💻 **For Loop vs While Loop:**
                1. **For Loop:** जब आपको पता हो कि काम कितनी बार दोहराना है।
                ```python
                for i in range(1, 6):
                    print("Dost number", i, "ko Happy Birthday!")
                ```

                2. **While Loop:** जब तक कोई शर्त (Condition) सच (True) है, तब तक लूप चलता रहेगा।
                ```python
                battery = 100
                while battery > 0:
                    print("Phone on hai, battery:", battery)
                    battery -= 25
                ```

                ⚡ **Common Mistake:** While loop में काउंटर को अपडेट करना न भूलें, वरना 'Infinite Loop' बन जाएगा!
            """.trimIndent()
        }

        if (q.contains("function") || q.contains("def")) {
            return """
                🤖 **MD Lukman AI Tutor:**

                **Functions** प्रोग्रामिंग का सबसे पॉवरफुल टूल हैं!

                🛠️ **Real-Life Example:**
                सोचिए एक 'Juicer Machine'। आप उसमें फल (Input) डालते हैं, वह जूस निकालती है (Process), और आपको फ्रेश जूस (Output) मिलता है।
                Python में Function भी एक ऐसी मशीन (Code block) है जिसे एक बार बनाते हैं और बार-बार इस्तेमाल करते हैं।

                💻 **Code Example:**
                ```python
                def make_tea(cups, sugar):
                    recipe = f"{cups} cup chai bani {sugar} chammach cheeni ke sath!"
                    return recipe

                # Function call karna:
                my_chai = make_tea(2, 1)
                print(my_chai)
                ```

                🎯 **Kyu banate hain?**
                Code duplication से बचने के लिए! एक बार logic लिखो और जहाँ ज़रूरत हो वहाँ call कर लो (DRY: Don't Repeat Yourself).
            """.trimIndent()
        }

        if (q.contains("list") || q.contains("tuple") || q.contains("difference")) {
            return """
                🤖 **MD Lukman AI Tutor:**

                **Lists vs Tuples** का अंतर बहुत ही आसान है:

                🛍️ **Lists (Mutable - बदलाव संभव):**
                जैसे आपका Shopping List! आप बाज़ार गए, कोई नया सामान याद आया तो लिस्ट में जोड़ दिया (`append`), या कोई सामान खरीद लिया तो काट दिया (`remove`).
                ```python
                fruits = ["Aam", "Kela", "Seb"]
                fruits.append("Santra")  # List badal sakti hai
                ```

                🔒 **Tuples (Immutable - बदलाव नामुमकिन):**
                जैसे आपका Aadhaar Number या Date of Birth! एक बार बन गया तो कभी बदला नहीं जा सकता।
                ```python
                dob = (15, "August", 1947)  # Isme changes nahi ho sakte
                ```

                💡 **Summary:** अगर डेटा फिक्स रखना है तो `Tuple ()` यूज़ करें, अगर डेटा में बदलाव करना है तो `List []` यूज़ करें!
            """.trimIndent()
        }

        if (q.contains("oop") || q.contains("class") || q.contains("object")) {
            return """
                🤖 **MD Lukman AI Tutor:**

                **Object-Oriented Programming (OOPs)** को आसान शब्दों में समझते हैं:

                🏭 **Class vs Object:**
                - **Class** = एक Blueprint (नक्शा / सांचा) जैसे कार का डिज़ाइन।
                - **Object** = उस नक्शे से बनी असली चीज़ (Real Entity) जैसे आपके गैराज में खड़ी असली कार।

                💻 **Python Code Example:**
                ```python
                class Car:
                    def __init__(self, brand, color):
                        self.brand = brand  # Property
                        self.color = color

                    def start(self):       # Method (Action)
                        print(f"{self.color} {self.brand} car start ho gayi! Vroom Vroom!")

                # Object banana:
                my_car = Car("Tata Nexon", "Blue")
                my_car.start()
                ```

                🌟 **4 Pillars of OOPs:**
                1. **Encapsulation:** डेटा को सुरक्षित रखना।
                2. **Inheritance:** पेरेंट क्लास के गुण चाइल्ड क्लास में आना।
                3. **Polymorphism:** एक ही नाम, अलग-अलग काम।
                4. **Abstraction:** गैर-ज़रूरी डिटेल्स छुपाना।
            """.trimIndent()
        }

        // Generic comprehensive response
        return """
            🤖 **MD LUKMAN PYTHON AI - Aapka Personal Python Tutor:**

            नमस्ते! आपने पूछा: "$question"

            💡 **आसान भाषा में स्पष्टीकरण:**
            Python दुनिया की सबसे आसान और लोकप्रिय प्रोग्रामिंग भाषाओं में से एक है। इसकी syntax साधारण English की तरह होती है।

            📌 **मुख्य टिप्स (Best Practices):**
            1. Python में हर code blocks के लिए Indentation (4 spaces) का उपयोग होता है, curly brackets `{}` का नहीं।
            2. Python Dynamically Typed भाषा है, यानी आपको variable का type पहले से डिक्लेयर करने की ज़रूरत नहीं है।
            3. हर कॉन्सेप्ट को समझने के लिए हमारे **Code Lab** में खुद कोड लिखकर 'Run' बटन दबाएं।

            🚀 **अगला कदम:**
            - आप स्क्रीन के नीचे **'Code Lab'** में जाकर इसका अभ्यास कर सकते हैं।
            - यदि कोई खास एरर आ रहा है, तो बेझिझक पूरा कोड यहाँ पेस्ट करें, मैं लाइन-बाय-लाइन हल करके समझाऊंगा!
        """.trimIndent()
    }
}
