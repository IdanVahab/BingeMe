package com.example.bingeme.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the SettingsFragment, responsible for managing user preferences,
 * such as language and genres.
 */
class SettingsFragmentViewModel : ViewModel() {

    private val _preferredLanguage = MutableStateFlow("en")
    val preferredLanguage: StateFlow<String> get() = _preferredLanguage

    private val _preferredGenres = MutableStateFlow(listOf<String>())
    val preferredGenres: StateFlow<List<String>> get() = _preferredGenres

    /**
     * Updates the preferred language setting.
     *
     * @param language The new preferred language.
     */
    fun updatePreferredLanguage(language: String) {
        viewModelScope.launch {
            _preferredLanguage.emit(language)
        }
    }

    /**
     * Updates the list of preferred genres.
     *
     * @param genres The new list of preferred genres.
     */
    fun updatePreferredGenres(genres: List<String>) {
        viewModelScope.launch {
            _preferredGenres.emit(genres)
        }
    }
}
