package com.MovieDb.app.search;

import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_LATEST_RELEASE;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_NOW_PLAYING;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_POPULAR;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_RECOMMENDATIONS;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_TOP_RATED;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_UPCOMING;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MULTI_SEARCH;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_LATEST_RELEASE;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_ON_THE_AIR;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_POPULAR;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_RECOMMENDATIONS;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_TOP_RATED;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.MovieDb.app.R;
import com.MovieDb.app.core.data.repository.CatalogRepository;
import com.MovieDb.app.core.data.repository.Resource;
import com.MovieDb.app.core.data.source.local.entity.GenreEntity;
import com.MovieDb.app.core.data.source.remote.entity.MediaEntity;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final CatalogRepository repository;

    private int page;
    private String query;
    private int mediaId;
    private final MutableLiveData<Integer> queryType = new MutableLiveData<>();

    public SearchViewModel(Application application, CatalogRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setQueryType(int queryType) {
        this.queryType.setValue(queryType);
    }

    private final MutableLiveData<String> header = new MutableLiveData<>();
    private LiveData<Resource<List<MediaEntity>>> searchResult;
    private LiveData<Resource<List<GenreEntity>>> genres;

    public LiveData<String> getHeader() {
        return header;
    }

    public LiveData<Resource<List<MediaEntity>>> getSearchResult() {
        if (searchResult == null) {
            searchResult = Transformations.switchMap(queryType, input -> {
                switch (input) {
                    case QUERY_TYPE_MULTI_SEARCH:
                        header.postValue(getApplication().getString(R.string.search));
                        return repository.getMultiSearch(query, page);

                    case QUERY_TYPE_MOVIE_LATEST_RELEASE:
                        header.postValue(getApplication().getString(R.string.latest_release_movies));
                        return repository.getMovieLatestRelease(page);

                    case QUERY_TYPE_TV_LATEST_RELEASE:
                        header.postValue(getApplication().getString(R.string.latest_release_tv_shows));
                        return repository.getTVLatestRelease(page);

                    case QUERY_TYPE_MOVIE_NOW_PLAYING:
                        header.postValue(getApplication().getString(R.string.now_playing_movies));
                        return repository.getMovieNowPlaying(page);

                    case QUERY_TYPE_TV_ON_THE_AIR:
                        header.postValue(getApplication().getString(R.string.on_the_air_tv_shows));
                        return repository.getTVOnTheAir(page);

                    case QUERY_TYPE_MOVIE_UPCOMING:
                        header.postValue(getApplication().getString(R.string.upcoming_movies));
                        return repository.getMovieUpcoming(page);

                    case QUERY_TYPE_MOVIE_TOP_RATED:
                        header.postValue(getApplication().getString(R.string.top_rated_movies));
                        return repository.getMovieTopRated(page);

                    case QUERY_TYPE_TV_TOP_RATED:
                        header.postValue(getApplication().getString(R.string.top_rated_tv_shows));
                        return repository.getTVTopRated(page);

                    case QUERY_TYPE_MOVIE_POPULAR:
                        header.postValue(getApplication().getString(R.string.popular_movies));
                        return repository.getMoviePopular(page);

                    case QUERY_TYPE_TV_POPULAR:
                        header.postValue(getApplication().getString(R.string.popular_tv_shows));
                        return repository.getTVPopular(page);

                    case QUERY_TYPE_MOVIE_RECOMMENDATIONS:
                        header.postValue(getApplication().getString(R.string.recommendations_movies));
                        return repository.getMovieRecommendations(mediaId, page);

                    case QUERY_TYPE_TV_RECOMMENDATIONS:
                        header.postValue(getApplication().getString(R.string.recommendations_tv_shows));
                        return repository.getTVRecommendations(mediaId, page);

                    default:
                        return new MutableLiveData<>();
                }
            });
        }
        return searchResult;
    }

    public LiveData<Resource<List<GenreEntity>>> getGenres() {
        if (genres == null) genres = repository.getGenres();
        return genres;
    }
}
