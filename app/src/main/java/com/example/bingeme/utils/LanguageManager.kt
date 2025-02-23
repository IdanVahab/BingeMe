package com.example.bingeme.utils

import java.util.Locale

object LanguageManager {
    val apiLanguage: String
        get() = if (Locale.getDefault().language == "iw" || Locale.getDefault().language == "he") {
            "he-IL" // עברית
        } else {
            "en-US" // ברירת מחדל - אנגלית
        }
}
