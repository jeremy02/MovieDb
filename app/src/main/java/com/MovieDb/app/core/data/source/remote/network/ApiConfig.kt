package com.MovieDb.app.core.data.source.remote.network

import com.MovieDb.app.BuildConfig
import com.MovieDb.app.core.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    @JvmStatic
    val apiService: ApiService
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client: OkHttpClient = Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TMDB)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
}