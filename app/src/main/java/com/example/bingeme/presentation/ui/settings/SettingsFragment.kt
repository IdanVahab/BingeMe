package com.example.bingeme.presentation.ui.settings

/**
 * Fragment for managing application settings, such as preferred language and genres.
 * This class is currently empty but can be implemented to provide a user interface
 * for customizing app preferences.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.bingeme.R

class SettingsFragment : Fragment() {

    private lateinit var languageSpinner: Spinner
    private lateinit var darkModeSwitch: Switch
    private lateinit var genresContainer: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize views
        languageSpinner = view.findViewById(R.id.languageSpinner)
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)
        genresContainer = view.findViewById(R.id.genresContainer)

        // Load settings
        loadSettings()

        return view
    }

    private fun loadSettings() {
        // Load available languages
        val languages = arrayOf("English", "Hebrew", "Spanish")
        languageSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)

        // Load genres dynamically
        val genres = arrayOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi")
        genres.forEach { genre ->
            val checkBox = CheckBox(requireContext())
            checkBox.text = genre
            genresContainer.addView(checkBox)
        }

        // Load dark mode preference
        darkModeSwitch.isChecked = isDarkModeEnabled()

        // Handle events
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        // Return user's dark mode preference (default: false)
        return false
    }

    private fun setDarkMode(enabled: Boolean) {
        // Save dark mode preference
    }
}