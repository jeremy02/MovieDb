package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class CastItem(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("credit_id") val creditId: String,
    @field:SerializedName(
        "profile_path"
    ) val profilePath: String,
    @field:SerializedName(
        "known_for_department"
    ) val knownForDepartment: String,
    @field:SerializedName(
        "character"
    ) val character: String
)