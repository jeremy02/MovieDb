package com.MovieDb.app.main.discover;

import static com.MovieDb.app.core.utils.Constants.EXTRA_QUERY;
import static com.MovieDb.app.core.utils.Constants.EXTRA_QUERY_TYPE;
import static com.MovieDb.app.core.utils.Constants.ORIENTATION_TYPE_HORIZONTAL;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_LATEST_RELEASE;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_POPULAR;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_TOP_RATED;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MOVIE_UPCOMING;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_LATEST_RELEASE;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_POPULAR;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_TV_TOP_RATED;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.MovieDb.app.R;
import com.MovieDb.app.core.data.repository.Status;
import com.MovieDb.app.core.data.source.remote.entity.MediaEntity;
import com.MovieDb.app.core.ui.adapter.MediaAdapter;
import com.MovieDb.app.core.ui.viewmodel.ViewModelFactory;
import com.MovieDb.app.core.utils.ShimmerHelper;
import com.MovieDb.app.databinding.FragmentDiscoverBinding;
import com.MovieDb.app.search.SearchActivity;
import com.google.android.material.chip.ChipGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscoverFragment extends Fragment implements View.OnClickListener, ChipGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener {

    private DiscoverViewModel viewModel;
    private FragmentDiscoverBinding binding;
    private MediaAdapter movieAdapter, tvAdapter;
    private ShimmerHelper shimmerMovie, shimmerTV;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvViewMoreMovie.setOnClickListener(this);
        binding.tvViewMoreTv.setOnClickListener(this);

        binding.rvMovie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvMovie.setHasFixedSize(true);
        movieAdapter = new MediaAdapter(ORIENTATION_TYPE_HORIZONTAL);
        binding.rvMovie.setAdapter(movieAdapter);

        binding.rvTv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvTv.setHasFixedSize(true);
        tvAdapter = new MediaAdapter(ORIENTATION_TYPE_HORIZONTAL);
        binding.rvTv.setAdapter(tvAdapter);

        shimmerMovie = new ShimmerHelper(binding.shimmerMovie, binding.rvMovie);
        shimmerTV = new ShimmerHelper(binding.shimmerTv, binding.rvTv);

        if (getActivity() != null) {
            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
            viewModel = new ViewModelProvider(this, factory).get(DiscoverViewModel.class);
            viewModel.getGenres().observe(getViewLifecycleOwner(),
                    result -> {
                        if (result != null) {
                            if (result.status == Status.SUCCESS) {
                                movieAdapter.submitGenreList(result.data);
                                tvAdapter.submitGenreList(result.data);
                            }
                        }
                    });

            binding.chipGroup.setOnCheckedChangeListener(this);
            binding.chipPopular.setChecked(true);
            binding.searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
        shimmerMovie.show();
        shimmerTV.show();
        binding.lytTv.setVisibility(View.VISIBLE);
        if (checkedId == binding.chipPopular.getId()) {
            binding.tvMovie.setText(R.string.popular);
            binding.tvTv.setText(R.string.popular);
            viewModel.getMoviePopular().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setMovieList(result.data);
                    }
                }
            });
            viewModel.getTVPopular().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setTVList(result.data);
                    }
                }
            });
        } else if (checkedId == binding.chipUpcoming.getId()) {
            binding.tvMovie.setText(R.string.upcoming);
            viewModel.getMovieUpcoming().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setMovieList(result.data);
                    }
                }
            });
            binding.lytTv.setVisibility(View.INVISIBLE);
        } else if (checkedId == binding.chipLatestRelease.getId()) {
            binding.tvMovie.setText(R.string.latest_release);
            binding.tvTv.setText(R.string.latest_release);
            viewModel.getMovieLatestRelease().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setMovieList(result.data);
                    }
                }
            });
            viewModel.getTVLatestRelease().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setTVList(result.data);
                    }
                }
            });
        } else if (checkedId == binding.chipTopRated.getId()) {
            binding.tvMovie.setText(R.string.top_rated);
            binding.tvTv.setText(R.string.top_rated);
            viewModel.getMovieTopRated().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setMovieList(result.data);
                    }
                }
            });
            viewModel.getTVTopRated().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.status == Status.SUCCESS) {
                        setTVList(result.data);
                    }
                }
            });
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        binding.searchView.clearFocus();
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra(EXTRA_QUERY, query);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(@NotNull View view) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        int id = view.getId();
        if (id == binding.tvViewMoreMovie.getId()) {
            if (binding.chipPopular.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_MOVIE_POPULAR);
            } else if (binding.chipUpcoming.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_MOVIE_UPCOMING);
            } else if (binding.chipLatestRelease.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_MOVIE_LATEST_RELEASE);
            } else if (binding.chipTopRated.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_MOVIE_TOP_RATED);
            }
        } else if (id == binding.tvViewMoreTv.getId()) {
            if (binding.chipPopular.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_TV_POPULAR);
            } else if (binding.chipLatestRelease.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_TV_LATEST_RELEASE);
            } else if (binding.chipTopRated.isChecked()) {
                intent.putExtra(EXTRA_QUERY_TYPE, QUERY_TYPE_TV_TOP_RATED);
            }
        }
        startActivity(intent);
    }

    private void setMovieList(List<MediaEntity> result) {
        movieAdapter.submitList(result);
        if (!result.isEmpty()) binding.rvMovie.scrollToPosition(0);
        shimmerMovie.hide(result.isEmpty());
    }

    private void setTVList(List<MediaEntity> result) {
        tvAdapter.submitList(result);
        if (!result.isEmpty()) binding.rvTv.scrollToPosition(0);
        shimmerTV.hide(result.isEmpty());
    }
}