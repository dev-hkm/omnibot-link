package com.hkm.dictionary.data.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.hkm.dictionary.BuildConfig
import com.hkm.dictionary.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object GroqTranslationService {
    private const val API_URL = "https://api.groq.com/openai/v1/chat/completions"
    private const val MODEL = "llama-3.3-70b-versatile"
    private const val JSON_MEDIA_TYPE = "application/json; charset=utf-8"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private data class GroqRequest(
        val model: String,
        val messages: List<Message>,
        val temperature: Double = 0.3,
        @SerializedName("max_tokens") val maxTokens: Int = 4000
    )

    private data class Message(
        val role: String,
        val content: String
    )

    private data class GroqResponse(
        val choices: List<Choice>?,
        val error: GroqError?
    )

    private data class Choice(
        val message: Message
    )

    private data class GroqError(
        val message: String
    )

    suspend fun translate(wordEntries: List<WordEntry>): TranslatedWordResult = withContext(Dispatchers.IO) {
        val word = wordEntries.first().word
        val jsonInput = gson.toJson(wordEntries)

        val systemPrompt = """
Bạn là một chuyên gia dịch thuật từ điển Anh-Việt. Nhiệm vụ của bạn là dịch toàn bộ nội dung từ điển tiếng Anh sang tiếng Việt một cách chính xác và đầy đủ.

Dữ liệu đầu vào là JSON từ Free Dictionary API chứa: word, phonetics (phát âm), meanings (nghĩa, từ loại, định nghĩa, ví dụ), synonyms, antonyms, sourceUrls.

Bạn PHẢN HỒI DUY NHẤT một JSON hợp lệ theo cấu trúc sau, KHÔNG thêm bất kỳ text nào khác:

{
  "word": "từ gốc",
  "phonetic": "phiên âm gốc",
  "pronunciations": [
    {"text": "phiên âm", "audioUrl": "url audio nếu có", "region": "US hoặc UK"}
  ],
  "meanings": [
    {
      "partOfSpeech": "từ loại (đã dịch sang tiếng Việt: danh từ, động từ, tính từ...)",
      "vietnameseMeaning": "nghĩa tiếng Việt ngắn gọn của từ này",
      "englishDefinition": "định nghĩa gốc tiếng Anh",
      "synonyms": ["từ đồng nghĩa tiếng Anh"],
      "antonyms": ["từ trái nghĩa tiếng Anh"]
    }
  ],
  "examples": [
    {"english": "câu ví dụ gốc", "vietnamese": "dịch sang tiếng Việt"}
  ],
  "idioms": [
    {"english": "thành ngữ/cụm từ gốc", "vietnamese": "nghĩa tiếng Việt"}
  ],
  "synonyms": ["danh sách từ đồng nghĩa tổng hợp"],
  "antonyms": ["danh sách từ trái nghĩa tổng hợp"],
  "sourceUrls": ["url nguồn"]
}

QUY TẮC:
1. partOfSpeech dịch sang tiếng Việt: noun→danh từ, verb→động từ, adjective→tính từ, adverb→trạng từ, preposition→giới từ, conjunction→liên từ, pronoun→đại từ, interjection→thán từ, etc.
2. vietnameseMeaning là nghĩa tiếng Việt ngắn gọn, dễ hiểu
3. englishDefinition giữ nguyên định nghĩa gốc tiếng Anh
4. Nếu có example (câu ví dụ) trong dữ liệu, thêm vào mảng examples
5. Nếu có idioms/ thành ngữ, thêm vào mảng idioms
6. Đảm bảo output là JSON thuần túy, không markdown, không code block
        """.trimIndent()

        val userPrompt = "Hãy dịch dữ liệu từ điển sau đây sang tiếng Việt:\n\n$jsonInput"

        val groqRequest = GroqRequest(
            model = MODEL,
            messages = listOf(
                Message("system", systemPrompt),
                Message("user", userPrompt)
            ),
            temperature = 0.3,
            maxTokens = 4000
        )

        val requestBody = gson.toJson(groqRequest)
            .toRequestBody(JSON_MEDIA_TYPE.toMediaType())

        val request = Request.Builder()
            .url(API_URL)
            .header("Authorization", "Bearer ${BuildConfig.GROQ_API_KEY}")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()
            ?: throw Exception("Empty response from Groq API")

        if (!response.isSuccessful) {
            throw Exception("Groq API error ${response.code}: $body")
        }

        val groqResponse = gson.fromJson(body, GroqResponse::class.java)
        groqResponse.error?.let {
            throw Exception("Groq API error: ${it.message}")
        }

        val content = groqResponse.choices?.firstOrNull()?.message?.content
            ?: throw Exception("Groq returned empty content")

        try {
            val cleanedContent = content
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            gson.fromJson(cleanedContent, TranslatedWordResult::class.java)
        } catch (e: Exception) {
            throw Exception("Lỗi parse dữ liệu dịch: ${e.message}")
        }
    }
}
