package com.hkm.dictionary.data.repository

import com.hkm.dictionary.data.api.FreeDictionaryApi
import com.hkm.dictionary.data.api.GroqTranslationService
import com.hkm.dictionary.data.model.TranslatedWordResult

class DictionaryRepository {

    /**
     * Complete lookup flow:
     * 1. Fetch raw word data from Free Dictionary API
     * 2. Translate all content to Vietnamese via Groq
     * Returns the translated result.
     */
    suspend fun lookupWord(word: String): TranslatedWordResult {
        val wordEntries = FreeDictionaryApi.lookup(word)
        return GroqTranslationService.translate(wordEntries)
    }
}
