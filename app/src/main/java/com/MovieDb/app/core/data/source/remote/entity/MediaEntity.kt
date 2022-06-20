package com.MovieDb.app.core.data.source.remote.entity

import android.os.Parcel
import android.os.Parcelable
import com.MovieDb.app.core.data.source.local.entity.GenreEntity

class MediaEntity : Parcelable {
    val id: Int
    val title: String?
    val poster: String?
    val cover: String?
    val scoreAverage: Double
    val scoreCount: Int
    val popularity: Double
    val type: String?
    var episodes = 0
        private set
    var status: String? = null
        private set
    var airedDate: AiredDateEntity? = null
        private set
    var studios: List<StudioEntity>? = null
        private set
    var genreIds: List<Int>? = null
        private set
    var genres: List<GenreEntity>? = null
        private set
    var runtime = 0
        private set
    val synopsis: String?
    var trailers: List<TrailerEntity>? = null

    constructor(
        id: Int,
        title: String?,
        poster: String?,
        cover: String?,
        scoreAverage: Double,
        scoreCount: Int,
        popularity: Double,
        type: String?,
        airedDate: AiredDateEntity?,
        genreIds: List<Int>?,
        synopsis: String?
    ) {
        this.id = id
        this.title = title
        this.poster = poster
        this.cover = cover
        this.scoreAverage = scoreAverage
        this.scoreCount = scoreCount
        this.popularity = popularity
        this.type = type
        this.airedDate = airedDate
        this.genreIds = genreIds
        this.synopsis = synopsis
    }

    constructor(
        id: Int,
        title: String?,
        poster: String?,
        cover: String?,
        scoreAverage: Double,
        scoreCount: Int,
        popularity: Double,
        type: String?,
        episodes: Int,
        status: String?,
        airedDate: AiredDateEntity?,
        studios: List<StudioEntity>?,
        genres: List<GenreEntity>?,
        runtime: Int,
        synopsis: String?,
        trailers: List<TrailerEntity>?
    ) {
        this.id = id
        this.title = title
        this.poster = poster
        this.cover = cover
        this.scoreAverage = scoreAverage
        this.scoreCount = scoreCount
        this.popularity = popularity
        this.type = type
        this.episodes = episodes
        this.status = status
        this.airedDate = airedDate
        this.studios = studios
        this.genres = genres
        this.runtime = runtime
        this.synopsis = synopsis
        this.trailers = trailers
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        title = `in`.readString()
        poster = `in`.readString()
        cover = `in`.readString()
        scoreAverage = `in`.readDouble()
        scoreCount = `in`.readInt()
        popularity = `in`.readDouble()
        type = `in`.readString()
        episodes = `in`.readInt()
        status = `in`.readString()
        runtime = `in`.readInt()
        synopsis = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(poster)
        dest.writeString(cover)
        dest.writeDouble(scoreAverage)
        dest.writeInt(scoreCount)
        dest.writeDouble(popularity)
        dest.writeString(type)
        dest.writeInt(episodes)
        dest.writeString(status)
        dest.writeInt(runtime)
        dest.writeString(synopsis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MediaEntity?> = object : Parcelable.Creator<MediaEntity?> {
            override fun createFromParcel(`in`: Parcel): MediaEntity {
                return MediaEntity(`in`)
            }

            override fun newArray(size: Int): Array<MediaEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}