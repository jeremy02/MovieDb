package com.MovieDb.app.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FavoriteWithGenres(// parent
    @field:Embedded
    @JvmField
    val favorite: FavoriteEntity, @field:Relation(
        parentColumn = "uid",
        entity = GenreEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = FavoriteGenreJoin::class,
            parentColumn = "favoriteUid",
            entityColumn = "genreId"
        )
    )
    @JvmField
    val genres: List<GenreEntity>
)