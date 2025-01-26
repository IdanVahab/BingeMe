package com.example.bingeme.data.remote

import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Provides network-related dependencies for the application using Dagger-Hilt.
 * This module sets up the Retrofit instance and the TMDB API service.
 */
@Module
@InstallIn(SingletonComponent::class) // Specifies the module's scope to the entire application
object NetworkModule {

    /**
     * Provides a singleton Retrofit instance configured with the base URL
     * and GsonConverterFactory for JSON parsing.
     *
     * @return A configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Base URL for the TMDB API
            .addConverterFactory(GsonConverterFactory.create()) // JSON parser
            .build()
    }

    /**
     * Provides a singleton instance of the TMDB API service.
     * This service is used to interact with the TMDB API endpoints.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of the TmdbApiService interface.
     */
    @Provides
    @Singleton
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }
}
