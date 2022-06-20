package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class CrewItem(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("credit_id") val creditId: String,
    @field:SerializedName(
        "profile_path"
    ) val profilePath: String?,
    @field:SerializedName("job") val job: String
)