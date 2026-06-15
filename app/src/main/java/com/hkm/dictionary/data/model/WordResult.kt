package com.hkm.dictionary.data.model

import com.google.gson.annotations.SerializedName

/**
 * Root response from Free Dictionary API.
 * Returns an array of WordEntry objects.
 */
data class WordEntry(
    val word: String,
    val phonetic: String?,
    val phonetics: List<Phonetic>,
    val meanings: List<Meaning>,
    val license: License?,
    @SerializedName("source_urls") val sourceUrls: List<String>?
)

data class Phonetic(
    val text: String?,
    val audio: String?,
    val sourceUrl: String?,
    val license: License?
)

data class Meaning(
    @SerializedName("part_of_speech") val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>
)

data class Definition(
    val definition: String,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val example: String?
)

data class License(
    val name: String,
    val url: String
)

/**
 * Result model after translating all content via Groq.
 */
data class TranslatedWordResult(
    val word: String,
    val phonetic: String?,
    val pronunciations: List<TranslatedPron> = emptyList(),
    val meanings: List<TranslatedMeaning> = emptyList(),
    val examples: List<TranslatedExample> = emptyList(),
    val idioms: List<TranslatedIdiom> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val sourceUrls: List<String> = emptyList()
)

data class TranslatedPron(
    val text: String,
    val audioUrl: String?,
    val region: String // "US" or "UK"
)

data class TranslatedMeaning(
    val partOfSpeech: String,
    val vietnameseMeaning: String,
    val englishDefinition: String,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)

data class TranslatedExample(
    val english: String,
    val vietnamese: String
)

data class TranslatedIdiom(
    val english: String,
    val vietnamese: String
)
