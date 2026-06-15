package com.hkm.dictionary.ui.component

import android.media.MediaPlayer
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hkm.dictionary.data.model.TranslatedWordResult

@Composable
fun WordResultCard(
    result: TranslatedWordResult,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var expandedSection by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        // ===== HEADER: Word + Phonetic =====
        WordHeader(
            word = result.word,
            phonetic = result.phonetic,
            pronunciations = result.pronunciations,
            onPlayAudio = { url ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                playAudio(url)
            }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        // ===== MEANINGS =====
        if (result.meanings.isNotEmpty()) {
            SectionTitle("Nghĩa của từ")
            result.meanings.forEachIndexed { index, meaning ->
                MeaningCard(
                    partOfSpeech = meaning.partOfSpeech,
                    vietnameseMeaning = meaning.vietnameseMeaning,
                    englishDefinition = meaning.englishDefinition,
                    synonyms = meaning.synonyms,
                    antonyms = meaning.antonyms,
                    isExpanded = expandedSection == "meaning_$index",
                    onToggle = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        expandedSection = if (expandedSection == "meaning_$index") null else "meaning_$index"
                    }
                )
            }
        }

        // ===== EXAMPLES =====
        if (result.examples.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SectionTitle("Câu ví dụ")
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                result.examples.forEachIndexed { index, example ->
                    ExampleCard(
                        english = example.english,
                        vietnamese = example.vietnamese,
                        isExpanded = expandedSection == "example_$index",
                        onToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            expandedSection = if (expandedSection == "example_$index") null else "example_$index"
                        }
                    )
                    if (index < result.examples.lastIndex) Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // ===== IDIOMS =====
        if (result.idioms.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SectionTitle("Thành ngữ / Cụm từ")
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                result.idioms.forEach { idiom ->
                    IdiomCard(english = idiom.english, vietnamese = idiom.vietnamese)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // ===== SYNONYMS & ANTONYMS =====
        if (result.synonyms.isNotEmpty() || result.antonyms.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SynonymsAntonymsRow(
                synonyms = result.synonyms,
                antonyms = result.antonyms
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun WordHeader(
    word: String,
    phonetic: String?,
    pronunciations: List<com.hkm.dictionary.data.model.TranslatedPron>,
    onPlayAudio: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (!phonetic.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = phonetic,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Pronunciation audio buttons
        if (pronunciations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pronunciations.forEach { pron ->
                    val hasAudio = !pron.audioUrl.isNullOrEmpty()
                    FilledTonalButton(
                        onClick = { if (hasAudio) onPlayAudio(pron.audioUrl!!) },
                        enabled = hasAudio,
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (hasAudio) Icons.Filled.VolumeUp else Icons.Outlined.VolumeOff,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (pron.region == "US") "US" else "UK",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun MeaningCard(
    partOfSpeech: String,
    vietnameseMeaning: String,
    englishDefinition: String,
    synonyms: List<String>,
    antonyms: List<String>,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Part of speech chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = partOfSpeech,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Vietnamese meaning
            Text(
                text = vietnameseMeaning,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // English definition
            Text(
                text = englishDefinition,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Toggle expand for synonyms/antonyms
            if (synonyms.isNotEmpty() || antonyms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onToggle,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isExpanded) "Thu gọn" else "Xem thêm từ đồng nghĩa / trái nghĩa",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (isExpanded) {
                    if (synonyms.isNotEmpty()) {
                        Text(
                            text = "Đồng nghĩa: ${synonyms.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    if (antonyms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Trái nghĩa: ${antonyms.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExampleCard(
    english: String,
    vietnamese: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "\"$english\"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = vietnamese,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IdiomCard(english: String, vietnamese: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = english,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = vietnamese,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SynonymsAntonymsRow(
    synonyms: List<String>,
    antonyms: List<String>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (synonyms.isNotEmpty()) {
            Text(
                text = "Từ đồng nghĩa",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                synonyms.forEach { syn ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(syn, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        if (antonyms.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Từ trái nghĩa",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                antonyms.forEach { ant ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(ant, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }
    }
}

// ===== AUDIO PLAYBACK =====
private fun playAudio(url: String) {
    try {
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener { release() }
            setOnErrorListener { _, _, _ -> release(); true }
        }
    } catch (_: Exception) {
        // Silently fail - audio just won't play
    }
}
