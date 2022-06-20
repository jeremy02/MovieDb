package com.MovieDb.app.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {
    @JvmStatic
    fun getFilteredFavoriteQuery(filter: FilterFavorite): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM favoriteEntities")
        if (!filter.isShowAllMediaType) {
            simpleQuery.append(" WHERE type = ")
            if (filter.isShowMovieOnly) simpleQuery.append("'" + Constants.MEDIA_TYPE_MOVIE + "'") else if (filter.isShowTVOnly) simpleQuery.append(
                "'" + Constants.MEDIA_TYPE_TV + "'"
            )
        }
        simpleQuery.append(" ORDER BY ")
        if (filter.isSortedByTitle) simpleQuery.append("title ASC") else if (filter.isSortedByRating) simpleQuery.append(
            "scoreAverage DESC"
        ) else if (filter.isSortedByReleaseDate) simpleQuery.append("startDate DESC")
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}