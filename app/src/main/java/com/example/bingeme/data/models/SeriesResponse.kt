package com.example.bingeme.data.models

/**
 * Represents the response model for a series-related API request.
 * This class is a placeholder for parsing and managing the results from the API.
 *
 * Currently, the class is empty but can be expanded to include properties
 * such as the list of series, pagination details, or other metadata.
 */
data class SeriesResponse (
    val page: Int,                 // Current page number
    val results: List<Series>,      // List of movies for the current page
    val total_pages: Int,          // Total number of pages
    val total_results: Int         // Total number of movie results
)
