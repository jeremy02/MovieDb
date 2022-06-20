package com.MovieDb.app.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.MovieDb.app.core.data.repository.Resource
import com.MovieDb.app.core.data.source.local.LocalDataSource
import com.MovieDb.app.core.data.source.local.entity.FavoriteWithGenres
import com.MovieDb.app.core.data.source.local.entity.GenreEntity
import com.MovieDb.app.core.data.source.remote.RemoteDataSource
import com.MovieDb.app.core.data.source.remote.RemoteDataSource.*
import com.MovieDb.app.core.data.source.remote.network.ApiResponse.Companion.success
import com.MovieDb.app.core.utils.AppExecutors
import com.MovieDb.app.core.utils.Constants
import com.MovieDb.app.core.utils.DataDummy.generateDummyFavorite
import com.MovieDb.app.core.utils.DataDummy.generateDummyFavorites
import com.MovieDb.app.core.utils.DataDummy.generateDummyGenres
import com.MovieDb.app.core.utils.DataDummy.generateDummyTVDetails
import com.MovieDb.app.core.utils.DataDummy.generateDummyTVTopRated
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyGenres
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieCredits
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieDetails
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieLatestRelease
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieNowPlaying
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMoviePopular
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieRecommendations
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieTopRated
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieTrending
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieUpcoming
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMovieVideos
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyMultiSearch
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVCredits
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVDetails
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVLatestRelease
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVOnTheAir
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVPopular
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVRecommendations
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVTopRated
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVTrending
import com.MovieDb.app.core.utils.DataDummy.generateRemoteDummyTVVideos
import com.MovieDb.app.core.utils.FilterFavorite
import com.MovieDb.app.utils.LiveDataTestUtil
import com.MovieDb.app.utils.PagedListUtil
import com.MovieDb.app.utils.TestExecutor
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock

class CatalogRepositoryTest {
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val local = Mockito.mock(LocalDataSource::class.java)
    private val appExecutors = Mockito.mock(AppExecutors::class.java)
    private val catalogRepository = FakeCatalogRepository(remote, local, appExecutors)
    private val testExecutors = AppExecutors(TestExecutor(), TestExecutor())
    private val movieDetailsResponse = generateRemoteDummyMovieDetails()
    private val movieId = movieDetailsResponse.id
    private val tvDetailsResponse = generateRemoteDummyTVDetails()
    private val tvId = tvDetailsResponse.id
    private val page = 1

    @Test
    fun multiSearch() {
        val multiSearchResponse = generateRemoteDummyMultiSearch()
        val query = "the promised neverland"
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadMultiSearchCallback)
                .onMultiSearchReceived(success(multiSearchResponse))
            null
        }.`when`(remote).getMultiSearch(
            ArgumentMatchers.eq(query), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMultiSearchCallback::class.java
            )
        )
        val mediaEntities =
            LiveDataTestUtil.getValue(catalogRepository.getMultiSearch(query, page))
        Mockito.verify(remote).getMultiSearch(
            ArgumentMatchers.eq(query), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMultiSearchCallback::class.java
            )
        )
        Assert.assertNotNull(mediaEntities.data)
        Assert.assertEquals(
            multiSearchResponse.results.size.toLong(),
            mediaEntities.data!!.size.toLong()
        )
    }

    @Test
    fun movieDetails() {
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieDetailsCallback)
                .onMovieDetailsReceived(success(movieDetailsResponse))
            null
        }.`when`(remote).getMovieDetails(
            ArgumentMatchers.eq(movieId), ArgumentMatchers.any(
                LoadMovieDetailsCallback::class.java
            )
        )
        val movieDetailsEntity =
            LiveDataTestUtil.getValue(catalogRepository.getMovieDetails(movieId))
        Mockito.verify(remote).getMovieDetails(
            ArgumentMatchers.eq(movieId), ArgumentMatchers.any(
                LoadMovieDetailsCallback::class.java
            )
        )
        Assert.assertNotNull(movieDetailsEntity.data)
        Assert.assertEquals(
            movieDetailsResponse.id.toLong(),
            movieDetailsEntity.data!!.id.toLong()
        )
        Assert.assertEquals(
            movieDetailsResponse.id.toLong(),
            movieDetailsEntity.data!!.id.toLong()
        )
        Assert.assertEquals(movieDetailsResponse.title, movieDetailsEntity.data!!.title)
        Assert.assertEquals(movieDetailsResponse.posterPath, movieDetailsEntity.data!!.poster)
        Assert.assertEquals(movieDetailsResponse.backdropPath, movieDetailsEntity.data!!.cover)
        Assert.assertEquals(
            movieDetailsResponse.voteAverage,
            movieDetailsEntity.data!!.scoreAverage,
            0.0
        )
        Assert.assertEquals(
            movieDetailsResponse.voteCount.toLong(),
            movieDetailsEntity.data!!.scoreCount.toLong()
        )
        Assert.assertEquals(
            movieDetailsResponse.popularity,
            movieDetailsEntity.data!!.popularity,
            0.0
        )
        Assert.assertEquals(movieDetailsResponse.status, movieDetailsEntity.data!!.status)
        Assert.assertEquals(
            movieDetailsResponse.releaseDate,
            movieDetailsEntity.data!!.airedDate!!.startDate
        )
        Assert.assertEquals(
            movieDetailsResponse.productionCompanies.size.toLong(),
            movieDetailsEntity.data!!.studios!!.size.toLong()
        )
        Assert.assertEquals(
            movieDetailsResponse.genres.size.toLong(),
            movieDetailsEntity.data!!.genres!!.size.toLong()
        )
        Assert.assertEquals(
            movieDetailsResponse.runtime.toLong(),
            movieDetailsEntity.data!!.runtime
        )
        Assert.assertEquals(movieDetailsResponse.overview, movieDetailsEntity.data!!.synopsis)
    }

    @Test
    fun tVDetails() {
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVDetailsCallback)
                .onTVDetailsReceived(success(tvDetailsResponse))
            null
        }.`when`(remote).getTVDetails(
            ArgumentMatchers.eq(tvId), ArgumentMatchers.any(
                LoadTVDetailsCallback::class.java
            )
        )
        val tvDetailsEntity = LiveDataTestUtil.getValue(catalogRepository.getTVDetails(tvId))
        Mockito.verify(remote).getTVDetails(
            ArgumentMatchers.eq(tvId), ArgumentMatchers.any(
                LoadTVDetailsCallback::class.java
            )
        )
        Assert.assertNotNull(tvDetailsEntity.data)
        Assert.assertEquals(tvDetailsResponse.id.toLong(), tvDetailsEntity.data!!.id.toLong())
        Assert.assertEquals(tvDetailsResponse.name, tvDetailsEntity.data!!.title)
        Assert.assertEquals(tvDetailsResponse.posterPath, tvDetailsEntity.data!!.poster)
        Assert.assertEquals(tvDetailsResponse.backdropPath, tvDetailsEntity.data!!.cover)
        Assert.assertEquals(
            tvDetailsResponse.voteAverage,
            tvDetailsEntity.data!!.scoreAverage,
            0.0
        )
        Assert.assertEquals(
            tvDetailsResponse.voteCount.toLong(),
            tvDetailsEntity.data!!.scoreCount.toLong()
        )
        Assert.assertEquals(
            tvDetailsResponse.popularity,
            tvDetailsEntity.data!!.popularity,
            0.0
        )
        Assert.assertEquals(
            tvDetailsResponse.numberOfEpisodes.toLong(),
            tvDetailsEntity.data!!.episodes
        )
        Assert.assertEquals(tvDetailsResponse.status, tvDetailsEntity.data!!.status)
        Assert.assertEquals(
            tvDetailsResponse.firstAirDate,
            tvDetailsEntity.data!!.airedDate!!.startDate
        )
        Assert.assertEquals(
            tvDetailsResponse.lastAirDate,
            tvDetailsEntity.data!!.airedDate!!.endDate
        )
        Assert.assertEquals(
            tvDetailsResponse.productionCompanies.size.toLong(),
            tvDetailsEntity.data!!.studios!!.size.toLong()
        )
        Assert.assertEquals(
            tvDetailsResponse.genres.size.toLong(),
            tvDetailsEntity.data!!.genres!!.size.toLong()
        )
        Assert.assertEquals(
            tvDetailsResponse.episodeRunTime[0].toLong(),
            tvDetailsEntity.data!!.runtime
        )
        Assert.assertEquals(tvDetailsResponse.overview, tvDetailsEntity.data!!.synopsis)
    }

    @Test
    fun movieTrending() {
        val movieResponse = generateRemoteDummyMovieTrending()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieTrendingCallback)
                .onMovieTrendingReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieTrending(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieTrendingCallback::class.java
            )
        )
        val movieEntities = LiveDataTestUtil.getValue(catalogRepository.getMovieTrending(page))
        Mockito.verify(remote).getMovieTrending(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieTrendingCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun tVTrending() {
        val tvResponse = generateRemoteDummyTVTrending()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVTrendingCallback)
                .onTVTrendingReceived(success(tvResponse))
            null
        }.`when`(remote).getTVTrending(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVTrendingCallback::class.java
            )
        )
        val tvEntities = LiveDataTestUtil.getValue(catalogRepository.getTVTrending(page))
        Mockito.verify(remote).getTVTrending(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVTrendingCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun movieLatestRelease() {
        val movieResponse = generateRemoteDummyMovieLatestRelease()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieLatestReleaseCallback)
                .onMovieLatestReleaseReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieLatestRelease(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieLatestReleaseCallback::class.java
            )
        )
        val movieEntities =
            LiveDataTestUtil.getValue(catalogRepository.getMovieLatestRelease(page))
        Mockito.verify(remote).getMovieLatestRelease(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieLatestReleaseCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun VLatestRelease() {
        val tvResponse = generateRemoteDummyTVLatestRelease()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVLatestReleaseCallback)
                .onTVLatestReleaseReceived(success(tvResponse))
            null
        }.`when`(remote).getTVLatestRelease(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVLatestReleaseCallback::class.java
            )
        )
        val tvEntities = LiveDataTestUtil.getValue(catalogRepository.getTVLatestRelease(page))
        Mockito.verify(remote).getTVLatestRelease(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVLatestReleaseCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun movieNowPlaying() {
        val movieResponse = generateRemoteDummyMovieNowPlaying()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieNowPlayingCallback)
                .onMovieNowPlayingReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieNowPlaying(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieNowPlayingCallback::class.java
            )
        )
        val movieEntities =
            LiveDataTestUtil.getValue(catalogRepository.getMovieNowPlaying(page))
        Mockito.verify(remote).getMovieNowPlaying(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieNowPlayingCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun tVOnTheAir() {
        val tvResponse = generateRemoteDummyTVOnTheAir()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVOnTheAirCallback)
                .onTVOnTheAirReceived(success(tvResponse))
            null
        }.`when`(remote).getTVOnTheAir(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVOnTheAirCallback::class.java
            )
        )
        val tvEntities = LiveDataTestUtil.getValue(catalogRepository.getTVOnTheAir(page))
        Mockito.verify(remote).getTVOnTheAir(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVOnTheAirCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun movieUpcoming() {
        val movieResponse = generateRemoteDummyMovieUpcoming()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieUpcomingCallback)
                .onMovieUpcomingReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieUpcoming(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieUpcomingCallback::class.java
            )
        )
        val movieEntities = LiveDataTestUtil.getValue(catalogRepository.getMovieUpcoming(page))
        Mockito.verify(remote).getMovieUpcoming(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieUpcomingCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun movieTopRated() {
        val movieResponse = generateRemoteDummyMovieTopRated()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMovieTopRatedCallback)
                .onMovieTopRatedReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieTopRated(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieTopRatedCallback::class.java
            )
        )
        val movieEntities = LiveDataTestUtil.getValue(catalogRepository.getMovieTopRated(page))
        Mockito.verify(remote).getMovieTopRated(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieTopRatedCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun tVTopRated() {
        val tvResponse = generateRemoteDummyTVTopRated()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVTopRatedCallback)
                .onTVTopRatedReceived(success(tvResponse))
            null
        }.`when`(remote).getTVTopRated(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVTopRatedCallback::class.java
            )
        )
        val tvEntities = LiveDataTestUtil.getValue(catalogRepository.getTVTopRated(page))
        Mockito.verify(remote).getTVTopRated(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVTopRatedCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun moviePopular() {
        val movieResponse = generateRemoteDummyMoviePopular()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadMoviePopularCallback)
                .onMoviePopularReceived(success(movieResponse))
            null
        }.`when`(remote).getMoviePopular(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMoviePopularCallback::class.java
            )
        )
        val movieEntities = LiveDataTestUtil.getValue(catalogRepository.getMoviePopular(page))
        Mockito.verify(remote).getMoviePopular(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMoviePopularCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun tVPopular() {
        val tvResponse = generateRemoteDummyTVPopular()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[1] as LoadTVPopularCallback)
                .onTVPopularReceived(success(tvResponse))
            null
        }.`when`(remote).getTVPopular(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVPopularCallback::class.java
            )
        )
        val tvEntities = LiveDataTestUtil.getValue(catalogRepository.getTVPopular(page))
        Mockito.verify(remote).getTVPopular(
            ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVPopularCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun movieRecommendations() {
        val movieResponse = generateRemoteDummyMovieRecommendations()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadMovieRecommendationsCallback)
                .onMovieRecommendationsReceived(success(movieResponse))
            null
        }.`when`(remote).getMovieRecommendations(
            ArgumentMatchers.eq(movieId), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieRecommendationsCallback::class.java
            )
        )
        val movieEntities =
            LiveDataTestUtil.getValue(catalogRepository.getMovieRecommendations(movieId, page))
        Mockito.verify(remote).getMovieRecommendations(
            ArgumentMatchers.eq(movieId), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadMovieRecommendationsCallback::class.java
            )
        )
        Assert.assertNotNull(movieEntities.data)
        Assert.assertEquals(
            movieResponse.results.size.toLong(),
            movieEntities.data!!.size.toLong()
        )
    }

    @Test
    fun tVRecommendations() {
        val tvResponse = generateRemoteDummyTVRecommendations()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadTVRecommendationsCallback)
                .onTVRecommendationsReceived(success(tvResponse))
            null
        }.`when`(remote).getTVRecommendations(
            ArgumentMatchers.eq(tvId), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVRecommendationsCallback::class.java
            )
        )
        val tvEntities =
            LiveDataTestUtil.getValue(catalogRepository.getTVRecommendations(tvId, page))
        Mockito.verify(remote).getTVRecommendations(
            ArgumentMatchers.eq(tvId), ArgumentMatchers.eq(page), ArgumentMatchers.any(
                LoadTVRecommendationsCallback::class.java
            )
        )
        Assert.assertNotNull(tvEntities.data)
        Assert.assertEquals(tvResponse.results.size.toLong(), tvEntities.data!!.size.toLong())
    }

    @Test
    fun genres() {
        val genresResponse = generateRemoteDummyGenres()
        val dummyGenres = MutableLiveData<List<GenreEntity>>()
        dummyGenres.value = generateDummyGenres()
        Mockito.`when`(local.genres).thenReturn(dummyGenres)
        val genreEntities = LiveDataTestUtil.getValue(catalogRepository.genres)
        Mockito.verify(local).genres
        Assert.assertNotNull(genreEntities.data)
        Assert.assertEquals(
            genresResponse.genres.size.toLong(),
            genreEntities.data!!.size.toLong()
        )
    }

    @Test
    fun videos() {
        val movieVideosResponse = generateRemoteDummyMovieVideos()
        val tvVideosResponse = generateRemoteDummyTVVideos()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadVideosCallback)
                .onVideosReceived(success(movieVideosResponse))
            null
        }.`when`(remote).getVideos(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_MOVIE),
            ArgumentMatchers.eq(movieId),
            ArgumentMatchers.any(
                LoadVideosCallback::class.java
            )
        )
        val movieTrailerEntities =
            LiveDataTestUtil.getValue(
                catalogRepository.getVideos(
                    Constants.MEDIA_TYPE_MOVIE,
                    movieId
                )
            )
        Mockito.verify(remote).getVideos(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_MOVIE),
            ArgumentMatchers.eq(movieId),
            ArgumentMatchers.any(
                LoadVideosCallback::class.java
            )
        )
        Assert.assertNotNull(movieTrailerEntities.data)
        Assert.assertEquals(
            movieVideosResponse.results.size.toLong(),
            movieTrailerEntities.data!!.size.toLong()
        )
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadVideosCallback)
                .onVideosReceived(success(tvVideosResponse))
            null
        }.`when`(remote).getVideos(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_TV),
            ArgumentMatchers.eq(tvId),
            ArgumentMatchers.any(
                LoadVideosCallback::class.java
            )
        )
        val tvTrailerEntities =
            LiveDataTestUtil.getValue(
                catalogRepository.getVideos(
                    Constants.MEDIA_TYPE_TV,
                    tvId
                )
            )
        Mockito.verify(remote).getVideos(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_TV),
            ArgumentMatchers.eq(tvId),
            ArgumentMatchers.any(
                LoadVideosCallback::class.java
            )
        )
        Assert.assertNotNull(tvTrailerEntities.data)
        Assert.assertEquals(
            tvVideosResponse.results.size.toLong(),
            tvTrailerEntities.data!!.size.toLong()
        )
    }

    @Test
    fun credits() {
        val movieCreditsResponse = generateRemoteDummyMovieCredits()
        val tvCreditsResponse = generateRemoteDummyTVCredits()
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadCreditsCallback)
                .onCreditsReceived(success(movieCreditsResponse))
            null
        }.`when`(remote).getCredits(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_MOVIE),
            ArgumentMatchers.eq(movieId),
            ArgumentMatchers.any(
                LoadCreditsCallback::class.java
            )
        )
        val movieCreditsEntity =
            LiveDataTestUtil.getValue(
                catalogRepository.getCredits(
                    Constants.MEDIA_TYPE_MOVIE,
                    movieId
                )
            )
        Mockito.verify(remote).getCredits(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_MOVIE),
            ArgumentMatchers.eq(movieId),
            ArgumentMatchers.any(
                LoadCreditsCallback::class.java
            )
        )
        Assert.assertNotNull(movieCreditsEntity.data)
        Assert.assertEquals(
            movieCreditsResponse.cast.size.toLong(),
            movieCreditsEntity.data!!.casts.size.toLong()
        )
        Assert.assertEquals(
            movieCreditsResponse.crew.size.toLong(),
            movieCreditsEntity.data!!.crews.size.toLong()
        )
        Mockito.doAnswer { invocation: InvocationOnMock ->
            (invocation.arguments[2] as LoadCreditsCallback)
                .onCreditsReceived(success(tvCreditsResponse))
            null
        }.`when`(remote).getCredits(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_TV),
            ArgumentMatchers.eq(tvId),
            ArgumentMatchers.any(
                LoadCreditsCallback::class.java
            )
        )
        val tvCreditsEntity =
            LiveDataTestUtil.getValue(
                catalogRepository.getCredits(
                    Constants.MEDIA_TYPE_TV,
                    tvId
                )
            )
        Mockito.verify(remote).getCredits(
            ArgumentMatchers.eq(Constants.MEDIA_TYPE_TV),
            ArgumentMatchers.eq(tvId),
            ArgumentMatchers.any(
                LoadCreditsCallback::class.java
            )
        )
        Assert.assertNotNull(tvCreditsEntity.data)
        Assert.assertEquals(
            tvCreditsResponse.cast.size.toLong(),
            tvCreditsEntity.data!!.casts.size.toLong()
        )
        Assert.assertEquals(
            tvCreditsResponse.crew.size.toLong(),
            tvCreditsEntity.data!!.crews.size.toLong()
        )
    }

    @Test
    fun favorites() {
        val tvResponse = generateRemoteDummyTVTopRated()
        val filter = FilterFavorite()
        val dataSourceFactory: DataSource.Factory<*, *>? = Mockito.mock(
            DataSource.Factory::class.java
        )

        Mockito.`when`(local.getAllFavoriteWithGenres(filter))
            .thenReturn(dataSourceFactory as DataSource.Factory<Int, FavoriteWithGenres>?)

        catalogRepository.getFavorites(filter)
        val favoriteEntities = Resource.success(
            PagedListUtil
                .mockPagedList(generateDummyFavorites(generateDummyTVTopRated()))
        )
        Mockito.verify(local).getAllFavoriteWithGenres(filter)
        Assert.assertNotNull(favoriteEntities.data)
        Assert.assertEquals(
            tvResponse.results.size.toLong(),
            favoriteEntities.data!!.size.toLong()
        )
    }

    @Test
    fun favorite() {
        val dummyFavorite = MutableLiveData<FavoriteWithGenres>()
        dummyFavorite.value = generateDummyFavorite(generateDummyTVDetails())
        Mockito.`when`(local.getFavoriteWithGenresById(tvId, Constants.MEDIA_TYPE_TV))
            .thenReturn(dummyFavorite)
        val favoriteEntity =
            LiveDataTestUtil.getValue(
                catalogRepository.getFavorite(
                    tvId,
                    Constants.MEDIA_TYPE_TV
                )
            )
        Mockito.verify(local).getFavoriteWithGenresById(tvId, Constants.MEDIA_TYPE_TV)
        Assert.assertNotNull(favoriteEntity)
        Assert.assertEquals(Integer.valueOf(tvDetailsResponse.id), favoriteEntity.favorite.id)
        Assert.assertEquals(tvDetailsResponse.name, favoriteEntity.favorite.title)
        Assert.assertEquals(tvDetailsResponse.posterPath, favoriteEntity.favorite.poster)
        Assert.assertEquals(
            tvDetailsResponse.voteAverage,
            favoriteEntity.favorite.scoreAverage,
            0.0
        )
        Assert.assertEquals(tvDetailsResponse.firstAirDate, favoriteEntity.favorite.startDate)
        Assert.assertEquals(
            tvDetailsResponse.genres.size.toLong(),
            favoriteEntity.genres.size.toLong()
        )
    }

    @Test
    fun setFavorite() {
        val dummyFavorite = generateDummyFavorite(generateDummyTVDetails())
        val favorite = dummyFavorite.favorite
        favorite.genres = dummyFavorite.genres
        Mockito.`when`(appExecutors.diskIO()).thenReturn(testExecutors.diskIO())
        Mockito.doNothing().`when`(local).setFavorite(favorite, true)
        catalogRepository.setFavorite(favorite, true)
        Mockito.verify(local, Mockito.times(1)).setFavorite(favorite, true)
    }

    @Test
    fun updateFavorite() {
        val dummyFavorite = generateDummyFavorite(generateDummyTVDetails())
        val favorite = dummyFavorite.favorite
        favorite.genres = dummyFavorite.genres
        Mockito.`when`(appExecutors.diskIO()).thenReturn(testExecutors.diskIO())
        Mockito.doNothing().`when`(local).updateFavorite(favorite)
        catalogRepository.updateFavorite(favorite)
        Mockito.verify(local, Mockito.times(1)).updateFavorite(favorite)
    }
}