package com.MovieDb.app.search;

import static com.MovieDb.app.core.utils.Constants.EXTRA_MEDIA_ID;
import static com.MovieDb.app.core.utils.Constants.EXTRA_QUERY;
import static com.MovieDb.app.core.utils.Constants.EXTRA_QUERY_TYPE;
import static com.MovieDb.app.core.utils.Constants.ORIENTATION_TYPE_VERTICAL;
import static com.MovieDb.app.core.utils.Constants.QUERY_TYPE_MULTI_SEARCH;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.MovieDb.app.R;
import com.MovieDb.app.core.data.repository.Resource;
import com.MovieDb.app.core.data.repository.Status;
import com.MovieDb.app.core.data.source.remote.entity.MediaEntity;
import com.MovieDb.app.core.ui.adapter.MediaAdapter;
import com.MovieDb.app.core.ui.viewmodel.ViewModelFactory;
import com.MovieDb.app.core.utils.ShimmerHelper;
import com.MovieDb.app.databinding.ActivitySearchBinding;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ActivitySearchBinding binding;
    private MediaAdapter adapter;
    private SearchViewModel viewModel;
    private ShimmerHelper shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabBack.setOnClickListener(view -> onBackPressed());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new MediaAdapter(ORIENTATION_TYPE_VERTICAL);
        binding.recyclerView.setAdapter(adapter);

        shimmer = new ShimmerHelper(binding.shimmer, binding.recyclerView, binding.lytEmpty);
        shimmer.show();

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
        viewModel.setPage(1);

        viewModel.getSearchResult().observe(this, observer);
        viewModel.getHeader().observe(this, header -> binding.tvHeader.setText(header));
        viewModel.getGenres().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    adapter.submitGenreList(result.data);
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_QUERY)) {
            String query = intent.getStringExtra(EXTRA_QUERY);
            binding.searchView.setQuery(query, true);
            performQuery(query);
        } else if (intent.hasExtra(EXTRA_QUERY_TYPE)) {
            // For recommendations section
            if (intent.hasExtra(EXTRA_MEDIA_ID)) {
                int mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, 0);
                viewModel.setMediaId(mediaId);
            }
            int type = intent.getIntExtra(EXTRA_QUERY_TYPE, 0);
            viewModel.setQueryType(type);
        }

        binding.searchView.setOnQueryTextListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(() ->
                binding.swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        binding.searchView.clearFocus();
        boolean hasSubmittedQuery = getIntent().hasExtra(EXTRA_QUERY) &&
                !query.equals(getIntent().getStringExtra(EXTRA_QUERY));
        if (adapter.getItemCount() == 0 && !hasSubmittedQuery) {
            binding.scrollView.scrollTo(0, 0);
            binding.appBar.setExpanded(true);
            performQuery(query);
        } else {
            // If there is already data in the adapter, we will open a new activity
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(EXTRA_QUERY, query);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void performQuery(String query) {
        binding.tvHeader.setText(R.string.search);
        viewModel.setQuery(query);
        viewModel.setQueryType(QUERY_TYPE_MULTI_SEARCH);
    }

    private final Observer<Resource<List<MediaEntity>>> observer = new Observer<Resource<List<MediaEntity>>>() {
        @Override
        public void onChanged(Resource<List<MediaEntity>> result) {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    adapter.submitList(result.data);
                    shimmer.hide(adapter.getItemCount() == 0);
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };
}