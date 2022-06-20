package com.MovieDb.app.core.utils

object Constants {
    const val BASE_URL_YOUTUBE = "https://youtu.be/"
    const val BASE_URL_TMDB = "https://api.themoviedb.org/3/"
    const val BASE_URL_TMDB_IMAGE = "https://image.tmdb.org/t/p/"
    const val EXTRA_BOOKMARK_FILTER = "extra_favorite_filter"
    const val EXTRA_MEDIA_TYPE = "extra_media_type"
    const val EXTRA_MEDIA_ID = "extra_movie_id"
    const val EXTRA_QUERY = "extra_query"
    const val EXTRA_QUERY_TYPE = "extra_query_type"
    const val IMAGE_SIZE_NORMAL = "w500"
    const val IMAGE_SIZE_HIGH = "original"
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_TV = "tv"
    const val MOVIE_STATUS_PLANNED = "Planned"
    const val MOVIE_STATUS_PRE_PRODUCTION = "Pre Production"
    const val MOVIE_STATUS_IN_PRODUCTION = "In Production"
    const val MOVIE_STATUS_POST_PRODUCTION = "Post Production"
    const val MOVIE_STATUS_RELEASED = "Released"
    const val ORIENTATION_TYPE_VERTICAL = 0
    const val ORIENTATION_TYPE_HORIZONTAL = 1
    const val QUERY_TYPE_MULTI_SEARCH = 100

    //int QUERY_TYPE_MOVIE_TRENDING = 201;
    const val QUERY_TYPE_MOVIE_LATEST_RELEASE = 202
    const val QUERY_TYPE_MOVIE_NOW_PLAYING = 203
    const val QUERY_TYPE_MOVIE_UPCOMING = 204
    const val QUERY_TYPE_MOVIE_TOP_RATED = 205
    const val QUERY_TYPE_MOVIE_POPULAR = 206
    const val QUERY_TYPE_MOVIE_RECOMMENDATIONS = 207

    //int QUERY_TYPE_TV_TRENDING = 301;
    const val QUERY_TYPE_TV_LATEST_RELEASE = 302
    const val QUERY_TYPE_TV_ON_THE_AIR = 303
    const val QUERY_TYPE_TV_TOP_RATED = 305
    const val QUERY_TYPE_TV_POPULAR = 306
    const val QUERY_TYPE_TV_RECOMMENDATIONS = 307
    const val TV_STATUS_RETURNING_SERIES = "Returning Series"
    const val TV_STATUS_ENDED = "Ended"
    const val VIDEO_SITE_YOUTUBE = "YouTube"
    const val VIDEO_TYPE_TRAILER = "Trailer"
    const val VIDEO_TYPE_TEASER = "Teaser"
}