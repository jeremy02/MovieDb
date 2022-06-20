package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class GenreItem(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String
)