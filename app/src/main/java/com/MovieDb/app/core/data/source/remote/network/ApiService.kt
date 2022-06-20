package com.MovieDb.app.core.data.source.remote.network

import com.MovieDb.app.core.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/multi")
    fun getMultiSearch(
        @Query("api_key") apiKey: String?,
        @Query("query") query: String?,
        @Query("page") page: Int
    ): Call<MultiSearchResponse?>?

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String?
    ): Call<MovieDetailsResponse?>?

    @GET("tv/{tv_id}")
    fun getTVDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String?
    ): Call<TVDetailsResponse?>?

    @GET("trending/movie/day")
    fun getMovieTrending(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("trending/tv/day")
    fun getTVTrending(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("discover/movie")
    fun getMovieLatestRelease(
        @Query("api_key") apiKey: String?,
        @Query("primary_release_date.gte") todaysDateGTE: String?,
        @Query("primary_release_date.lte") todaysDateLTE: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("discover/tv")
    fun getTVLatestRelease(
        @Query("api_key") apiKey: String?,
        @Query("first_air_date.gte") todaysDateGTE: String?,
        @Query("first_air_date.lte") todaysDateLTE: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("movie/now_playing")
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("tv/on_the_air")
    fun getTVOnTheAir(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("movie/upcoming")
    fun getMovieUpcoming(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("movie/top_rated")
    fun getMovieTopRated(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("tv/top_rated")
    fun getTVTopRated(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("movie/popular")
    fun getMoviePopular(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("tv/popular")
    fun getTVPopular(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("movie/{movie_id}/recommendations")
    fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<MovieResponse?>?

    @GET("tv/{tv_id}/recommendations")
    fun getTVRecommendations(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Call<TVResponse?>?

    @GET("genre/{media_type}/list")
    fun getGenres(
        @Path("media_type") mediaType: String?,
        @Query("api_key") apiKey: String?
    ): Call<GenresResponse?>?

    @GET("{media_type}/{media_id}/videos")
    fun getVideos(
        @Path("media_type") mediaType: String?,
        @Path("media_id") mediaId: Int,
        @Query("api_key") apiKey: String?
    ): Call<VideosResponse?>?

    @GET("{media_type}/{media_id}/credits")
    fun getCredits(
        @Path("media_type") mediaType: String?,
        @Path("media_id") mediaId: Int,
        @Query("api_key") apiKey: String?
    ): Call<CreditsResponse?>?
}