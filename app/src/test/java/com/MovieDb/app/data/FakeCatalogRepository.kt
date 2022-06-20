package com.MovieDb.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.MovieDb.app.core.data.repository.CatalogDataSource
import com.MovieDb.app.core.data.repository.NetworkBoundResource
import com.MovieDb.app.core.data.repository.Resource
import com.MovieDb.app.core.data.source.local.LocalDataSource
import com.MovieDb.app.core.data.source.local.entity.FavoriteEntity
import com.MovieDb.app.core.data.source.local.entity.FavoriteWithGenres
import com.MovieDb.app.core.data.source.local.entity.GenreEntity
import com.MovieDb.app.core.data.source.remote.RemoteDataSource
import com.MovieDb.app.core.data.source.remote.entity.*
import com.MovieDb.app.core.data.source.remote.network.ApiResponse
import com.MovieDb.app.core.data.source.remote.response.*
import com.MovieDb.app.core.utils.AppExecutors
import com.MovieDb.app.core.utils.Constants
import com.MovieDb.app.core.utils.FilterFavorite

class FakeCatalogRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : CatalogDataSource {
    override fun getMultiSearch(
        query: String,
        page: Int
    ): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMultiSearch(query, page) { (_, body) ->
            if (body != null) {
                val mediaList = multiSearchResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieDetails(movieId: Int): MutableLiveData<Resource<MediaEntity>> {
        val result = MutableLiveData<Resource<MediaEntity>>()
        remoteDataSource.getMovieDetails(movieId) { (_, body) ->
            if (body != null) {
                val media = movieDetailsResponseToMedia(body)
                result.postValue(Resource.success(media))
            }
        }
        return result
    }

    override fun getTVDetails(tvId: Int): MutableLiveData<Resource<MediaEntity>> {
        val result = MutableLiveData<Resource<MediaEntity>>()
        remoteDataSource.getTVDetails(tvId) { (_, body) ->
            if (body != null) {
                val media = tvDetailsResponseToMedia(body)
                result.postValue(Resource.success(media))
            }
        }
        return result
    }

    override fun getMovieTrending(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieTrending(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVTrending(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVTrending(page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieLatestRelease(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieLatestRelease(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVLatestRelease(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVLatestRelease(page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieNowPlaying(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieNowPlaying(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVOnTheAir(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVOnTheAir(page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieUpcoming(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieUpcoming(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieTopRated(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieTopRated(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVTopRated(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVTopRated(page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMoviePopular(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMoviePopular(page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVPopular(page: Int): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVPopular(page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getMovieRecommendations(
        movieId: Int,
        page: Int
    ): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getMovieRecommendations(movieId, page) { (_, body) ->
            if (body != null) {
                val mediaList = movieResponseToMediaList(
                    body
                )
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getTVRecommendations(
        tvId: Int,
        page: Int
    ): MutableLiveData<Resource<List<MediaEntity>>> {
        val result = MutableLiveData<Resource<List<MediaEntity>>>()
        remoteDataSource.getTVRecommendations(tvId, page) { (_, body) ->
            if (body != null) {
                val mediaList = tvResponsesToMediaList(body)
                result.postValue(Resource.success(mediaList))
            }
        }
        return result
    }

    override fun getVideos(
        mediaType: String,
        mediaId: Int
    ): MutableLiveData<Resource<List<TrailerEntity>>> {
        val result = MutableLiveData<Resource<List<TrailerEntity>>>()
        remoteDataSource.getVideos(mediaType, mediaId) { (_, body) ->
            if (body != null) {
                val trailerList = videosResponseToTrailerList(
                    body
                )
                result.postValue(Resource.success(trailerList))
            }
        }
        return result
    }

    override fun getCredits(
        mediaType: String,
        mediaId: Int
    ): MutableLiveData<Resource<CreditsEntity>> {
        val result = MutableLiveData<Resource<CreditsEntity>>()
        remoteDataSource.getCredits(mediaType, mediaId) { (_, body) ->
            if (body != null) {
                val credit = creditsResponseToCredit(body)
                result.postValue(Resource.success(credit))
            }
        }
        return result
    }

    override fun getFavorites(filter: FilterFavorite): LiveData<PagedList<FavoriteWithGenres>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(
            localDataSource.getAllFavoriteWithGenres(filter),
            config
        ).build()
    }

    override fun getFavorite(id: Int, type: String): LiveData<FavoriteWithGenres> {
        return localDataSource.getFavoriteWithGenresById(id, type)
    }

    override fun setFavorite(favorite: FavoriteEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setFavorite(favorite, state) }
    }

    override fun updateFavorite(favorite: FavoriteEntity) {
        appExecutors.diskIO().execute { localDataSource.updateFavorite(favorite) }
    }

    override fun getGenres(): LiveData<Resource<List<GenreEntity?>?>> {
        return object : NetworkBoundResource<List<GenreEntity?>?, GenresResponse?>(
            appExecutors
        ) {
            override fun loadFromDB(): LiveData<List<GenreEntity?>?>? {
                return localDataSource.genres
            }

            override fun shouldFetch(data: List<GenreEntity?>?): Boolean {
                if (data != null) {
                    return data.size < 27
                }
                return false
            }

            override fun createCall(): LiveData<ApiResponse<GenresResponse?>>? {
                return remoteDataSource.genres
            }

            override fun saveCallResult(data: GenresResponse?) {
                val genreList = data?.let { genreResponseToGenreList(it) }
                localDataSource.insertGenres(genreList)
            }
        }.asLiveData()
    }

    /* Convert  to MediaEntity */
    private fun movieItemToMedia(item: MovieItem): MediaEntity {
        return MediaEntity(
            item.id,
            item.title,
            item.posterPath,
            item.backdropPath,
            item.voteAverage,
            item.voteCount,
            item.popularity,
            Constants.MEDIA_TYPE_MOVIE,
            AiredDateEntity(item.releaseDate),
            item.genreIds,
            item.overview
        )
    }

    /* Convert  to MediaEntity */
    private fun tvItemToMedia(item: TVItem): MediaEntity {
        return MediaEntity(
            item.id,
            item.name,
            item.posterPath,
            item.backdropPath,
            item.voteAverage,
            item.voteCount,
            item.popularity,
            Constants.MEDIA_TYPE_TV,
            AiredDateEntity(item.firstAirDate, ""),
            item.genreIds,
            item.overview
        )
    }

    /* Convert  to list of MediaEntity */
    private fun movieResponseToMediaList(response: MovieResponse): List<MediaEntity> {
        val mediaList: MutableList<MediaEntity> = ArrayList()
        for (movie in response.results) {
            mediaList.add(movieItemToMedia(movie))
        }
        return mediaList
    }

    private fun tvResponsesToMediaList(response: TVResponse): List<MediaEntity> {
        val mediaList: MutableList<MediaEntity> = ArrayList()
        for (tv in response.results) {
            mediaList.add(tvItemToMedia(tv))
        }
        return mediaList
    }

    private fun multiSearchResponseToMediaList(response: MultiSearchResponse): List<MediaEntity> {
        val mediaList: MutableList<MediaEntity> = ArrayList()
        for (searchResult in response.results) {
            if (searchResult.mediaType == Constants.MEDIA_TYPE_MOVIE) {
                mediaList.add(
                    MediaEntity(
                        searchResult.id,
                        searchResult.title,
                        searchResult.posterPath,
                        searchResult.backdropPath,
                        searchResult.voteAverage,
                        searchResult.voteCount,
                        searchResult.popularity,
                        Constants.MEDIA_TYPE_MOVIE,
                        AiredDateEntity(searchResult.releaseDate!!),
                        searchResult.genreIds,
                        searchResult.overview
                    )
                )
            } else if (searchResult.mediaType == Constants.MEDIA_TYPE_TV) {
                mediaList.add(
                    MediaEntity(
                        searchResult.id,
                        searchResult.name,
                        searchResult.posterPath,
                        searchResult.backdropPath,
                        searchResult.voteAverage,
                        searchResult.voteCount,
                        searchResult.popularity,
                        Constants.MEDIA_TYPE_TV,
                        AiredDateEntity(searchResult.firstAirDate!!, ""),
                        searchResult.genreIds,
                        searchResult.overview
                    )
                )
            }
        }
        return mediaList
    }

    private fun movieDetailsResponseToMedia(response: MovieDetailsResponse): MediaEntity {
        val genreList = genresItemToGenreList(response.genres)
        val studioList = productionCompaniesItemToStudioList(
            response.productionCompanies
        )
        return MediaEntity(
            response.id,
            response.title,
            response.posterPath,
            response.backdropPath,
            response.voteAverage,
            response.voteCount,
            response.popularity,
            Constants.MEDIA_TYPE_MOVIE,
            1,
            response.status,
            AiredDateEntity(response.releaseDate),
            studioList,
            genreList,
            response.runtime,
            response.overview,
            null
        )
    }

    private fun tvDetailsResponseToMedia(response: TVDetailsResponse): MediaEntity {
        val genreList = genresItemToGenreList(response.genres)
        val studioList = productionCompaniesItemToStudioList(
            response.productionCompanies
        )
        return MediaEntity(
            response.id,
            response.name,
            response.posterPath,
            response.backdropPath,
            response.voteAverage,
            response.voteCount,
            response.popularity,
            Constants.MEDIA_TYPE_TV,
            response.numberOfEpisodes,
            response.status,
            AiredDateEntity(response.firstAirDate, response.lastAirDate),
            studioList,
            genreList,
            if (response.episodeRunTime.isEmpty()) 0 else response.episodeRunTime[0],
            response.overview,
            null
        )
    }

    private fun productionCompaniesItemToStudioList(items: List<ProductionCompanyItem>): List<StudioEntity> {
        val studioList: MutableList<StudioEntity> = ArrayList()
        for (studio in items) {
            studioList.add(StudioEntity(studio.id, studio.name, studio.logoPath))
        }
        return studioList
    }

    private fun genresItemToGenreList(items: List<GenreItem>): List<GenreEntity> {
        val genreList: MutableList<GenreEntity> = ArrayList()
        for (genre in items) {
            genreList.add(GenreEntity(genre.id, genre.name))
        }
        return genreList
    }

    private fun genreResponseToGenreList(response: GenresResponse): List<GenreEntity> {
        val genreList: MutableList<GenreEntity> = ArrayList()
        for (genre in response.genres) {
            genreList.add(GenreEntity(genre.id, genre.name))
        }
        return genreList
    }

    private fun videosResponseToTrailerList(response: VideosResponse): List<TrailerEntity> {
        val trailerList: MutableList<TrailerEntity> = ArrayList()
        for (video in response.results) {
            trailerList.add(
                TrailerEntity(
                    video.id,
                    video.name,
                    video.site,
                    video.type,
                    video.key
                )
            )
        }
        return trailerList
    }

    private fun creditsResponseToCredit(response: CreditsResponse): CreditsEntity {
        val castList: MutableList<CastEntity> = ArrayList()
        for (cast in response.cast) {
            castList.add(
                CastEntity(
                    cast.id,
                    cast.name,
                    cast.creditId,
                    cast.profilePath,
                    cast.knownForDepartment,
                    cast.character
                )
            )
        }
        val crewList: MutableList<CrewEntity> = ArrayList()
        for (crew in response.crew) {
            crewList.add(
                CrewEntity(
                    crew.id,
                    crew.name,
                    crew.creditId,
                    crew.profilePath,
                    crew.job
                )
            )
        }
        return CreditsEntity(response.id, castList, crewList)
    }
}