package com.example.data.local.converters

import androidx.room.TypeConverter
import com.example.model.QuestionType
import com.example.model.QuizQuestion
import org.json.JSONArray
import org.json.JSONObject

class RoomTypeConverters {

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        for (item in list) {
            array.put(item)
        }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(data)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (_: Exception) {
            // fallback
        }
        return list
    }

    @TypeConverter
    fun fromQuizQuestionList(questions: List<QuizQuestion>?): String {
        if (questions == null) return "[]"
        val array = JSONArray()
        for (q in questions) {
            val obj = JSONObject().apply {
                put("id", q.id)
                put("question", q.question)
                put("hindiQuestion", q.hindiQuestion)
                put("codeSnippet", q.codeSnippet ?: "")
                val optsArray = JSONArray()
                for (opt in q.options) {
                    optsArray.put(opt)
                }
                put("options", optsArray)
                put("correctOptionIndex", q.correctOptionIndex)
                put("hindiExplanation", q.hindiExplanation)
                put("questionType", q.questionType.name)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toQuizQuestionList(data: String?): List<QuizQuestion> {
        if (data.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<QuizQuestion>()
        try {
            val array = JSONArray(data)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val optsArray = obj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optsArray.length()) {
                    options.add(optsArray.getString(j))
                }
                val codeSnippet = obj.optString("codeSnippet", "").ifEmpty { null }
                val typeName = obj.optString("questionType", QuestionType.MCQ.name)
                val type = try { QuestionType.valueOf(typeName) } catch (_: Exception) { QuestionType.MCQ }

                list.add(
                    QuizQuestion(
                        id = obj.getInt("id"),
                        question = obj.getString("question"),
                        hindiQuestion = obj.getString("hindiQuestion"),
                        codeSnippet = codeSnippet,
                        options = options,
                        correctOptionIndex = obj.getInt("correctOptionIndex"),
                        hindiExplanation = obj.getString("hindiExplanation"),
                        questionType = type
                    )
                )
            }
        } catch (_: Exception) {
            // fallback
        }
        return list
    }
}
