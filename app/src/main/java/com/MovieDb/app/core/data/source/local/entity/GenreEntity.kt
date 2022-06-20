package com.MovieDb.app.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genreEntities")
class GenreEntity(
    @field:ColumnInfo(name = "id") @field:PrimaryKey val id: Int, @field:ColumnInfo(
        name = "name"
    ) val name: String
) {

    override fun toString(): String {
        return "GenreEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }
}