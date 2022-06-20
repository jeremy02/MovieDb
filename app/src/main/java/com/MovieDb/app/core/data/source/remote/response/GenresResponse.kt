package com.MovieDb.app.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class GenresResponse(@field:SerializedName("genres") val genres: List<GenreItem>)