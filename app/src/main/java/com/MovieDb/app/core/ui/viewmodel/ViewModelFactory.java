package com.MovieDb.app.core.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.MovieDb.app.core.data.repository.CatalogRepository;
import com.MovieDb.app.core.di.Injection;
import com.MovieDb.app.detail.DetailMediaViewModel;
import com.MovieDb.app.main.discover.DiscoverViewModel;
import com.MovieDb.app.main.favorite.FavoriteViewModel;
import com.MovieDb.app.main.movie.MovieViewModel;
import com.MovieDb.app.main.tv.TVViewModel;
import com.MovieDb.app.search.SearchViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;
    private final CatalogRepository mCatalogRepository;

    private ViewModelFactory(Application application, CatalogRepository catalogRepository) {
        mApplication = application;
        mCatalogRepository = catalogRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                INSTANCE = new ViewModelFactory(application,
                        Injection.provideRepository(application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel(mCatalogRepository);
        } else if (modelClass.isAssignableFrom(TVViewModel.class)) {
            return (T) new TVViewModel(mCatalogRepository);
        } else if (modelClass.isAssignableFrom(DiscoverViewModel.class)) {
            return (T) new DiscoverViewModel(mCatalogRepository);
        } else if (modelClass.isAssignableFrom(DetailMediaViewModel.class)) {
            return (T) new DetailMediaViewModel(mCatalogRepository);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(mApplication, mCatalogRepository);
        } else if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
            return (T) new FavoriteViewModel(mCatalogRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
