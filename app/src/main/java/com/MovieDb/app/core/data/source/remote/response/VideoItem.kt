package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class VideoItem(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("site") val site: String,
    @field:SerializedName(
        "type"
    ) val type: String,
    @field:SerializedName("key") val key: String
)