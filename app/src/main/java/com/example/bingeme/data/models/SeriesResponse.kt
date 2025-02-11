package com.example.bingeme.data.models

/**
 * Represents the response model for a paginated series API request.
 * This data class is used to parse and manage the results from the API.
 *
 * @param page The current page number in the paginated results.
 * @param results The list of series returned by the API for the current page.
 * @param total_pages The total number of pages available in the API results.
 * @param total_results The total number of series results available.
 */
data class SeriesResponse(
    val page: Int,                 // Current page number
    val results: List<Series>,      // List of series for the current page
    val total_pages: Int,          // Total number of pages
    val total_results: Int         // Total number of series results
)
