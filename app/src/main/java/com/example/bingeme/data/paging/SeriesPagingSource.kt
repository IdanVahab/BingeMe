package com.example.bingeme.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bingeme.data.models.Series
import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.LanguageManager

class SeriesPagingSource(
    private val apiService: TmdbApiService,
    private val apiKey: String
) : PagingSource<Int, Series>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Series> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getPopularTVSeries(apiKey, page = page, language = LanguageManager.apiLanguage)
            val series = response.body()?.results ?: emptyList()

            LoadResult.Page(
                data = series,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (series.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Series>): Int? {
        return state.anchorPosition
    }
}
