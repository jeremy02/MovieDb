package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class MultiSearchResponse(
    @field:SerializedName("page") val page: Int,
    @field:SerializedName(
        "total_pages"
    ) val totalPages: Int,
    @field:SerializedName("results") val results: List<SearchResultItem>,
    @field:SerializedName(
        "total_results"
    ) val totalResults: Int
)