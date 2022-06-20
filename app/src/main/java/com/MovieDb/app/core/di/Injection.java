package com.MovieDb.app.core.di;

import android.content.Context;

import com.MovieDb.app.core.data.repository.CatalogRepository;
import com.MovieDb.app.core.data.source.local.LocalDataSource;
import com.MovieDb.app.core.data.source.local.persistence.CatalogDatabase;
import com.MovieDb.app.core.data.source.remote.RemoteDataSource;
import com.MovieDb.app.core.utils.AppExecutors;

public class Injection {

    public static CatalogRepository provideRepository(Context context) {
        CatalogDatabase database = CatalogDatabase.getInstance(context);
        LocalDataSource localDataSource = LocalDataSource.getInstance(database.catalogDao());
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
        AppExecutors appExecutors = new AppExecutors();
        return CatalogRepository.getInstance(remoteDataSource, localDataSource, appExecutors);
    }
}
