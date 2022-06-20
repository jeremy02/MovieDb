package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class TVDetailsResponse(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName(
        "poster_path"
    ) val posterPath: String,
    @field:SerializedName(
        "backdrop_path"
    ) val backdropPath: String,
    @field:SerializedName("vote_average") val voteAverage: Double,
    @field:SerializedName("vote_count") val voteCount: Int,
    @field:SerializedName(
        "popularity"
    ) val popularity: Double,
    @field:SerializedName("number_of_seasons") val numberOfSeasons: Int,
    @field:SerializedName(
        "number_of_episodes"
    ) val numberOfEpisodes: Int,
    @field:SerializedName("status") val status: String,
    @field:SerializedName("first_air_date") val firstAirDate: String,
    @field:SerializedName(
        "last_air_date"
    ) val lastAirDate: String,
    @field:SerializedName(
        "production_companies"
    ) val productionCompanies: List<ProductionCompanyItem>,
    @field:SerializedName(
        "genres"
    ) val genres: List<GenreItem>,
    @field:SerializedName("episode_run_time") val episodeRunTime: List<Int>,
    @field:SerializedName(
        "overview"
    ) val overview: String
)