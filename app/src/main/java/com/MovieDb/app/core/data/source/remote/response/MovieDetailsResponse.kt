package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class MovieDetailsResponse(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("title") val title: String,
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
    @field:SerializedName("status") val status: String,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName(
        "production_companies"
    ) val productionCompanies: List<ProductionCompanyItem>,
    @field:SerializedName(
        "genres"
    ) val genres: List<GenreItem>,
    @field:SerializedName("runtime") val runtime: Int,
    @field:SerializedName("overview") val overview: String
)