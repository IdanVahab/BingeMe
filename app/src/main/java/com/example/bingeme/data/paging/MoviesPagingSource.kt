package com.example.bingeme.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.remote.TmdbApiService

class MoviesPagingSource(
    private val apiService: TmdbApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getPopularMovies(apiKey, page = page)
            val movies = response.body()?.results ?: emptyList()

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}
