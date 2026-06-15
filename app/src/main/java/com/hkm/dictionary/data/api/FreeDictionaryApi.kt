package com.hkm.dictionary.data.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hkm.dictionary.data.model.WordEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object FreeDictionaryApi {
    private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /**
     * Fetch word data from Free Dictionary API.
     * Returns list of WordEntry, or throws an exception on failure.
     */
    suspend fun lookup(word: String): List<WordEntry> = withContext(Dispatchers.IO) {
        val url = "$BASE_URL/${word.lowercase().trim()}"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()
            ?: throw Exception("Empty response from Dictionary API")

        if (!response.isSuccessful) {
            val errorMsg = when (response.code) {
                404 -> "Không tìm thấy từ \"$word\""
                429 -> "Quá nhiều request, thử lại sau"
                else -> "Lỗi API: ${response.code}"
            }
            throw Exception(errorMsg)
        }

        val type = object : TypeToken<List<WordEntry>>() {}.type
        gson.fromJson(body, type)
    }
}
