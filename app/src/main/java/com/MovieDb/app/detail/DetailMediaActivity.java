package com.MovieDb.app.detail;

import static com.MovieDb.app.core.utils.AppUtils.loadImage;
import static com.MovieDb.app.core.utils.AppUtils.showToast;
import static com.MovieDb.app.core.utils.Constants.BASE_URL_YOUTUBE;
import static com.MovieDb.app.core.utils.Constants.EXTRA_MEDIA_ID;
import static com.MovieDb.app.core.utils.Constants.EXTRA_MEDIA_TYPE;
import static com.MovieDb.app.core.utils.Constants.EXTRA_QUERY_TYPE;
import static com.MovieDb.app.core.utils.Constants.IMAGE_SIZE_HIGH;
import static com.MovieDb.app.core.utils.Constants.IMAGE_SIZE_NORMAL;
import static com.MovieDb.app.core.utils.Constants.MEDIA_TYPE_MOVIE;
import static com.MovieDb.app.core.utils.Constants.MEDIA_TYPE_TV;
import static com.MovieDb.app.core.utils.Constants.MOVIE_STATUS_IN_PRODUCTION;
import static com.MovieDb.app.core.utils.Constants.MOVIE_STATUS_PLANNED;
import static com.MovieDb.app.core.utils.Constants.MOVIE_STATUS_POST_PRODUCTION;
import static com.MovieDb.app.core.utils.Constants.MOVIE_STATUS_PRE_PRODUCTION;
import static com.MovieDb.app.core.utils.Constants.MOVIE_STATUS_RELEASED;
import static com.MovieDb.app.core.utils.Constants.ORIENTATION_TYPE_HORIZONTAL;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_RECOMMENDATIONS;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_RECOMMENDATIONS;
import static com.MovieDb.app.core.utils.Constants.TV_STATUS_ENDED;
import static com.MovieDb.app.core.utils.Constants.TV_STATUS_RETURNING_SERIES;
import static com.MovieDb.app.core.utils.Constants.VIDEO_SITE_YOUTUBE;
import static com.MovieDb.app.core.utils.Constants.VIDEO_TYPE_TEASER;
import static com.MovieDb.app.core.utils.Constants.VIDEO_TYPE_TRAILER;
import static com.MovieDb.app.core.utils.DateUtils.getDateWithoutYear;
import static com.MovieDb.app.core.utils.DateUtils.getYearOfDate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.MovieDb.app.R;
import com.MovieDb.app.core.data.repository.Status;
import com.MovieDb.app.core.data.source.local.entity.FavoriteEntity;
import com.MovieDb.app.core.data.source.local.entity.FavoriteWithGenres;
import com.MovieDb.app.core.data.source.local.entity.GenreEntity;
import com.MovieDb.app.core.data.source.remote.entity.MediaEntity;
import com.MovieDb.app.core.data.source.remote.entity.TrailerEntity;
import com.MovieDb.app.core.ui.adapter.GenreAdapter;
import com.MovieDb.app.core.ui.adapter.MediaAdapter;
import com.MovieDb.app.core.ui.viewmodel.ViewModelFactory;
import com.MovieDb.app.core.utils.ShimmerHelper;
import com.MovieDb.app.databinding.ActivityDetailMediaBinding;
import com.MovieDb.app.databinding.LayoutDetailMediaBinding;
import com.MovieDb.app.search.SearchActivity;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailMediaActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDetailMediaBinding activityBinding;
    private LayoutDetailMediaBinding contentBinding;

    private DetailMediaViewModel viewModel;
    private FavoriteWithGenres favorite;
    private Intent trailerIntent;
    private MediaAdapter adapterRecommendation;
    private MediaEntity media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityDetailMediaBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        contentBinding = activityBinding.lytDetailMedia;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setSupportActionBar(activityBinding.toolbar);
        activityBinding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                // Collapsed
                activityBinding.appBar.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.blue)));
                activityBinding.tvToolbarTitle.setVisibility(View.VISIBLE);
                activityBinding.fabBack.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
                activityBinding.fabBack.setElevation(0);
                activityBinding.fabFavorite.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
                activityBinding.fabFavorite.setElevation(0);
            } else {
                //Expanded
                activityBinding.appBar.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
                activityBinding.tvToolbarTitle.setVisibility(View.INVISIBLE);
                activityBinding.fabBack.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
                activityBinding.fabBack.setElevation(8);
                activityBinding.fabFavorite.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
                activityBinding.fabFavorite.setElevation(8);
            }
        });

        contentBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == 0) {
                        contentBinding.viewShadow.setVisibility(View.INVISIBLE);
                    } else {
                        contentBinding.viewShadow.setVisibility(View.VISIBLE);
                    }
                });

        adapterRecommendation = new MediaAdapter(ORIENTATION_TYPE_HORIZONTAL);

        activityBinding.fabBack.setOnClickListener(this);
        activityBinding.fabFavorite.setOnClickListener(this);
        contentBinding.ibMoreTitle.setOnClickListener(this);
        activityBinding.btnTrailer.setOnClickListener(this);
        contentBinding.tvViewMoreSynopsis.setOnClickListener(this);
        contentBinding.tvViewMoreRecommendation.setOnClickListener(this);

        ShimmerHelper shimmerRecommendation = new ShimmerHelper(
                contentBinding.shimmerRecommendation, contentBinding.rvRecommendation);
        shimmerRecommendation.show();

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(DetailMediaViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String mediaType = bundle.getString(EXTRA_MEDIA_TYPE);
            int mediaId = bundle.getInt(EXTRA_MEDIA_ID);
            viewModel.setMedia(mediaType, mediaId);

            viewModel.getMediaDetails().observe(this, result -> {
                if (result.status == Status.SUCCESS) {
                    if (result.data != null) {
                        populateMedia(result.data);
                    }
                }
            });
            viewModel.getCredits().observe(this, result -> {
            });
            viewModel.getFavorite().observe(this, this::setFavoriteState);
            viewModel.getGenres().observe(this, resultGenre -> {
                if (resultGenre != null) {
                    if (resultGenre.status == Status.SUCCESS) {
                        adapterRecommendation.submitGenreList(resultGenre.data);
                    }
                }
            });
            viewModel.getRecommendations().observe(this, resultMedia -> {
                if (resultMedia != null) {
                    if (resultMedia.status == Status.SUCCESS) {
                        if (resultMedia.data != null) {
                            populateRecommendations(resultMedia.data);
                            shimmerRecommendation.hide(resultMedia.data.isEmpty());
                        }
                    }
                }
            });
        }
    }

    private void populateMedia(@NotNull MediaEntity media) {
        this.media = media;

        activityBinding.tvToolbarTitle.setText(media.getTitle());
        loadImage(activityBinding.imgCover, media.getCover() == null ? media.getPoster() : media.getCover(), IMAGE_SIZE_HIGH);
        loadImage(contentBinding.imgPoster, media.getPoster(), IMAGE_SIZE_NORMAL);
        contentBinding.tvScoreAverage.setText(String.valueOf(media.getScoreAverage()));
        contentBinding.tvScoreCount.setText(String.valueOf(media.getScoreCount()));
        contentBinding.tvTitle.setText(media.getTitle());
        contentBinding.tvReleaseYear.setText(getYearOfDate(media.getAiredDate().getStartDate()));
        contentBinding.tvReleaseDate.setText(getResources().getString(R.string.formatted_release_date, getDateWithoutYear(media.getAiredDate().getStartDate())));
        contentBinding.tvSynopsis.setText(media.getSynopsis());

        if (!media.getStudios().isEmpty()) {
            contentBinding.tvStudio.setText(media.getStudios().get(0).getName());
        } else {
            contentBinding.tvStudio.setVisibility(View.GONE);
        }

        String type = null;
        if (media.getType().equals(MEDIA_TYPE_MOVIE)) {
            type = getResources().getString(R.string.movie);
            contentBinding.tvRuntime.setText(getResources().getString(R.string.runtime_movie, media.getRuntime()));
        } else if (media.getType().equals(MEDIA_TYPE_TV)) {
            type = getResources().getString(R.string.series);
            contentBinding.tvRuntime.setText(getResources().getString(R.string.runtime_tv, media.getRuntime()));
        }

        String status;
        switch (media.getStatus()) {
            case MOVIE_STATUS_PLANNED:
                status = MOVIE_STATUS_PLANNED;
                break;
            case MOVIE_STATUS_PRE_PRODUCTION:
                status = MOVIE_STATUS_PRE_PRODUCTION;
                break;
            case MOVIE_STATUS_IN_PRODUCTION:
                status = MOVIE_STATUS_IN_PRODUCTION;
                break;
            case MOVIE_STATUS_POST_PRODUCTION:
                status = MOVIE_STATUS_POST_PRODUCTION;
                break;
            case MOVIE_STATUS_RELEASED:
                status = MOVIE_STATUS_RELEASED;
                break;
            case TV_STATUS_RETURNING_SERIES:
                status = TV_STATUS_RETURNING_SERIES;
                break;
            case TV_STATUS_ENDED:
                status = TV_STATUS_ENDED;
                break;
            default:
                status = media.getStatus();
        }

        contentBinding.tvType.setText(getResources().getString(R.string.type, type,
                media.getEpisodes(),
                getResources().getQuantityString(R.plurals.number_of_episodes, media.getEpisodes()),
                status));

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        contentBinding.rvGenre.setLayoutManager(layoutManager);
        contentBinding.rvGenre.setHasFixedSize(true);
        GenreAdapter genreAdapter = new GenreAdapter();
        contentBinding.rvGenre.setAdapter(genreAdapter);

        List<String> genreStringList = new ArrayList<>();
        for (GenreEntity genre : media.getGenres()) {
            genreStringList.add(genre.getName());
        }
        genreAdapter.setData(genreStringList);

        viewModel.getTrailers().observe(this, result -> {
            if (result.status == Status.SUCCESS) {
                if (result.data != null) {
                    media.setTrailers(result.data);
                    initTrailerIntent();
                }
            }
        });
    }

    private void setFavoriteState(FavoriteWithGenres resultFavorite) {
        this.favorite = resultFavorite;

        boolean state = favorite != null;
        if (state) {
            activityBinding.fabFavorite.setImageDrawable(ContextCompat
                    .getDrawable(this, R.drawable.ic_favorite));
        } else {
            activityBinding.fabFavorite.setImageDrawable(ContextCompat
                    .getDrawable(this, R.drawable.ic_unfavorite));
        }
    }

    private void initTrailerIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String backupKey = "";
        if (!media.getTrailers().isEmpty()) {
            for (TrailerEntity trailer : media.getTrailers()) {
                if (trailer.getSite().equals(VIDEO_SITE_YOUTUBE)) {
                    if (trailer.getType().equals(VIDEO_TYPE_TRAILER) || trailer.getType().equals(VIDEO_TYPE_TEASER)) {
                        intent.setData(Uri.parse(BASE_URL_YOUTUBE + trailer.getKey()));
                        break;
                    } else if (backupKey.isEmpty()) {
                        backupKey = trailer.getKey();
                    }
                }
            }
        }
        if (intent.getData() == null) {
            if (!backupKey.isEmpty()) {
                intent.setData(Uri.parse(BASE_URL_YOUTUBE + backupKey));
            } else {
                intent.setData(Uri.parse(getResources().getString(
                        R.string.youtube_search_query, media.getTitle(),
                        getYearOfDate(media.getAiredDate().getStartDate()))));
            }
        }
        trailerIntent = intent;
    }

    private void populateRecommendations(@NotNull List<MediaEntity> mediaList) {
        if (mediaList.isEmpty()) {
            contentBinding.lytRecommendation.setVisibility(View.GONE);
        } else {
            contentBinding.rvRecommendation.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            contentBinding.rvRecommendation.setHasFixedSize(true);
            contentBinding.rvRecommendation.setAdapter(adapterRecommendation);
            adapterRecommendation.submitList(mediaList);
        }
    }

    @Override
    public void onClick(@NotNull View view) {
        int id = view.getId();
        if (id == activityBinding.fabBack.getId()) {
            onBackPressed();
        } else if (id == activityBinding.fabFavorite.getId()) {
            if (media != null) setFavorite(media, favorite != null);
            else showToast(this, getString(R.string.toast_data_loading));
        } else if (id == contentBinding.ibMoreTitle.getId()) {
            if (media != null) showToast(this, media.getTitle());
            else showToast(this, getString(R.string.toast_data_loading));
        } else if (id == activityBinding.btnTrailer.getId()) {
            if (trailerIntent != null) startActivity(trailerIntent);
            else showToast(this, getString(R.string.toast_data_loading));
        } else if (id == contentBinding.tvViewMoreSynopsis.getId()) {
            contentBinding.tvSynopsis.setMaxLines(Integer.MAX_VALUE);
            contentBinding.tvViewMoreSynopsis.setVisibility(View.GONE);
        } else if (id == contentBinding.tvViewMoreRecommendation.getId()) {
            viewMoreRecommendations(media);
        }
    }

    private void setFavorite(MediaEntity media, boolean state) {
        FavoriteEntity favorite = createFavoriteObject(media);
        viewModel.setFavorite(favorite, state);
    }

    @NotNull
    private FavoriteEntity createFavoriteObject(@NotNull MediaEntity media) {
        return new FavoriteEntity(media.getId(),
                media.getType(),
                media.getTitle(),
                media.getPoster(),
                media.getScoreAverage(),
                media.getAiredDate().getStartDate(),
                media.getGenres());
    }

    private void viewMoreRecommendations(MediaEntity media) {
        if (media != null) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(EXTRA_MEDIA_ID, media.getId());
            if (media.getType().equals(MEDIA_TYPE_MOVIE)) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_MOVIE_RECOMMENDATIONS);
            } else if (media.getType().equals(MEDIA_TYPE_TV)) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_TV_RECOMMENDATIONS);
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (media != null) viewModel.updateFavorite(favorite, createFavoriteObject(media));
    }
}