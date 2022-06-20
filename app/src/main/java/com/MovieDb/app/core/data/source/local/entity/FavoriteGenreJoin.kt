package com.MovieDb.app.core.data.source.local.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "favoriteGenreJoins",
    primaryKeys = ["favoriteUid", "genreId"],
    foreignKeys = [ForeignKey(
        entity = FavoriteEntity::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("favoriteUid"),
        onDelete = CASCADE
    ), ForeignKey(entity = GenreEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("genreId"))],
    indices = [Index(value = arrayOf("favoriteUid")), Index(value = arrayOf("genreId"))]
)
class FavoriteGenreJoin {
    @ColumnInfo(name = "favoriteUid")
    val favoriteUid: String

    @ColumnInfo(name = "genreId")
    val genreId: Int

    @ColumnInfo(name = "favoriteId")
    val favoriteId: Int

    @ColumnInfo(name = "favoriteType")
    val favoriteType: String

    constructor(favoriteUid: String, genreId: Int, favoriteId: Int, favoriteType: String) {
        this.favoriteUid = favoriteUid
        this.genreId = genreId
        this.favoriteId = favoriteId
        this.favoriteType = favoriteType
    }

    @Ignore
    constructor(favoriteId: Int, favoriteType: String, genreId: Int) {
        favoriteUid = favoriteType + favoriteId
        this.genreId = genreId
        this.favoriteId = favoriteId
        this.favoriteType = favoriteType
    }
}