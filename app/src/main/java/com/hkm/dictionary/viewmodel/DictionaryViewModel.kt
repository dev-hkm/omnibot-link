package com.hkm.dictionary.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkm.dictionary.data.model.TranslatedWordResult
import com.hkm.dictionary.data.repository.DictionaryRepository
import kotlinx.coroutines.launch

sealed class DictionaryUiState {
    data object Idle : DictionaryUiState()
    data object Loading : DictionaryUiState()
    data class Success(val result: TranslatedWordResult) : DictionaryUiState()
    data class Error(val message: String) : DictionaryUiState()
}

class DictionaryViewModel : ViewModel() {
    private val repository = DictionaryRepository()

    var uiState = mutableStateOf<DictionaryUiState>(DictionaryUiState.Idle)
        private set

    var searchQuery = mutableStateOf("")
        private set

    var hasSearched = mutableStateOf(false)
        private set

    fun updateQuery(query: String) {
        searchQuery.value = query
    }

    fun search() {
        val query = searchQuery.value.trim()
        if (query.isEmpty()) return

        hasSearched.value = true
        uiState.value = DictionaryUiState.Loading

        viewModelScope.launch {
            try {
                val result = repository.lookupWord(query)
                uiState.value = DictionaryUiState.Success(result)
            } catch (e: Exception) {
                uiState.value = DictionaryUiState.Error(
                    e.message ?: "Có lỗi xảy ra khi tra từ"
                )
            }
        }
    }

    fun clearSearch() {
        searchQuery.value = ""
        uiState.value = DictionaryUiState.Idle
        hasSearched.value = false
    }

    fun retry() {
        search()
    }
}
