package com.example.bingeme.data.local

import androidx.room.TypeConverter
import com.example.bingeme.data.models.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String {
        return gson.toJson(genres) // Convert List<Genre> to JSON String
    }

    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre> {
        if (genresString == null) return emptyList()
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genresString, listType) // Convert JSON String back to List<Genre>
    }
}
