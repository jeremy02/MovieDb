package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class VideosResponse(
    @field:SerializedName("id") val id: Int, @field:SerializedName(
        "results"
    ) val results: List<VideoItem>
)