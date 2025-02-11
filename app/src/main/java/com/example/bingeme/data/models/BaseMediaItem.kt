package com.example.bingeme.data.models

abstract class BaseMediaItem{
    abstract val id: Int
    abstract val title: String
    abstract val posterPath: String?
    abstract val overview: String?
    abstract val isFavorite: Boolean
    abstract val releaseDate: String?
}
