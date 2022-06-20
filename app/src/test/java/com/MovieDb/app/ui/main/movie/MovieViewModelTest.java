package com.MovieDb.app.ui.main.movie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.MovieDb.app.core.data.repository.CatalogRepository;
import com.MovieDb.app.core.data.repository.Resource;
import com.MovieDb.app.core.data.source.local.entity.GenreEntity;
import com.MovieDb.app.core.data.source.remote.entity.MediaEntity;
import com.MovieDb.app.core.utils.DataDummy;
import com.MovieDb.app.main.movie.MovieViewModel;
import com.MovieDb.app.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MovieViewModelTest {

    private MovieViewModel viewModel;

    private final int page = 1;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private Observer<Resource<List<MediaEntity>>> mediaEntitiesObserver;

    @Mock
    private Observer<Resource<List<GenreEntity>>> genreEntitiesObserver;

    @Before
    public void setUp() {
        viewModel = new MovieViewModel(catalogRepository);
        viewModel.setTrendingPage(page);
    }

    @Test
    public void getNowPlaying() {
        Resource<List<MediaEntity>> dummyMovieNowPlaying = Resource.success(DataDummy.generateDummyMovieNowPlaying());
        MutableLiveData<Resource<List<MediaEntity>>> nowPlaying = new MutableLiveData<>();
        nowPlaying.setValue(dummyMovieNowPlaying);

        when(catalogRepository.getMovieNowPlaying(page)).thenReturn(nowPlaying);
        Resource<List<MediaEntity>> movieEntities = LiveDataTestUtil.getValue(viewModel.getNowPlaying());
        verify(catalogRepository).getMovieNowPlaying(page);

        assertNotNull(dummyMovieNowPlaying.data);
        assertNotNull(movieEntities.data);
        assertEquals(dummyMovieNowPlaying.data.size(), movieEntities.data.size());

        viewModel.getNowPlaying().observeForever(mediaEntitiesObserver);
        verify(mediaEntitiesObserver).onChanged(dummyMovieNowPlaying);
    }

    @Test
    public void getTrending() {
        Resource<List<MediaEntity>> dummyMovieTrending = Resource.success(DataDummy.generateDummyMovieTrending());
        MutableLiveData<Resource<List<MediaEntity>>> trending = new MutableLiveData<>();
        trending.setValue(dummyMovieTrending);

        when(catalogRepository.getMovieTrending(page)).thenReturn(trending);
        Resource<List<MediaEntity>> movieEntities = LiveDataTestUtil.getValue(viewModel.getTrending());
        verify(catalogRepository).getMovieTrending(page);

        assertNotNull(dummyMovieTrending.data);
        assertNotNull(movieEntities.data);
        assertEquals(dummyMovieTrending.data.size(), movieEntities.data.size());

        viewModel.getTrending().observeForever(mediaEntitiesObserver);
        verify(mediaEntitiesObserver).onChanged(dummyMovieTrending);
    }

    @Test
    public void getGenres() {
        Resource<List<GenreEntity>> dummyGenres = Resource.success(DataDummy.generateDummyGenres());
        MutableLiveData<Resource<List<GenreEntity>>> movieGenres = new MutableLiveData<>();
        movieGenres.setValue(dummyGenres);

        when(catalogRepository.getGenres()).thenReturn(movieGenres);
        Resource<List<GenreEntity>> movieGenreEntities = LiveDataTestUtil.getValue(viewModel.getGenres());
        verify(catalogRepository).getGenres();

        assertNotNull(dummyGenres.data);
        assertNotNull(movieGenreEntities.data);
        assertEquals(dummyGenres.data.size(), movieGenreEntities.data.size());

        viewModel.getGenres().observeForever(genreEntitiesObserver);
        verify(genreEntitiesObserver).onChanged(dummyGenres);
    }
}