package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class CreditsResponse(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("cast")
    val cast: List<CastItem>,
    @field:SerializedName(
        "crew"
    ) val crew: List<CrewItem>
)
