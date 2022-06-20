package com.MovieDb.app.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteEntities")
class FavoriteEntity {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: String

    @ColumnInfo(name = "id")
    var id: Int

    @ColumnInfo(name = "type")
    var type: String

    @ColumnInfo(name = "title")
    var title: String

    @ColumnInfo(name = "poster")
    val poster: String

    @ColumnInfo(name = "scoreAverage")
    val scoreAverage: Double

    @ColumnInfo(name = "startDate")
    val startDate: String

    @Ignore
    var genres: List<GenreEntity>? = null

    constructor(
        uid: String,
        id: Int,
        type: String,
        title: String,
        poster: String,
        scoreAverage: Double,
        startDate: String
    ) {
        this.uid = uid
        this.id = id
        this.type = type
        this.title = title
        this.poster = poster
        this.scoreAverage = scoreAverage
        this.startDate = startDate
    }

    @Ignore
    constructor(
        id: Int,
        type: String,
        title: String,
        poster: String,
        scoreAverage: Double,
        startDate: String,
        genres: List<GenreEntity>?
    ) {
        uid = type + id
        this.id = id
        this.type = type
        this.title = title
        this.poster = poster
        this.scoreAverage = scoreAverage
        this.startDate = startDate
        this.genres = genres
    }
}