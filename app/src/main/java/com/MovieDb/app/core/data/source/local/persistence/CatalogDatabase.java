package com.MovieDb.app.core.data.source.local.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.MovieDb.app.core.data.source.local.entity.FavoriteEntity;
import com.MovieDb.app.core.data.source.local.entity.FavoriteGenreJoin;
import com.MovieDb.app.core.data.source.local.entity.GenreEntity;

@Database(entities = {FavoriteEntity.class, GenreEntity.class, FavoriteGenreJoin.class},
        version = 2,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class CatalogDatabase extends RoomDatabase {

    public abstract CatalogDao catalogDao();

    private static volatile CatalogDatabase INSTANCE;

    public static CatalogDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CatalogDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CatalogDatabase.class, "Catalog.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
