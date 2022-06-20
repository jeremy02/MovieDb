package com.MovieDb.app.main.favorite;

import static com.MovieDb.app.core.utils.Constants.EXTRA_BOOKMARK_FILTER;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.MovieDb.app.core.utils.FilterFavorite;
import com.MovieDb.app.databinding.DialogFilterFavoriteBinding;

import org.jetbrains.annotations.NotNull;

public class FilterFavoriteDialog extends DialogFragment implements View.OnClickListener {

    private AlertDialog dialog;
    private FavoriteFilterDialogListener listener;
    private FilterFavorite filter;
    private DialogFilterFavoriteBinding binding;

    public FilterFavoriteDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogFilterFavoriteBinding.inflate(getLayoutInflater());

        binding.ibClose.setOnClickListener(this);
        binding.tvReset.setOnClickListener(this);
        binding.btnApply.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);

        // Receive bundle from activity
        Bundle bundle = getArguments();
        if (bundle != null) filter = bundle.getParcelable(EXTRA_BOOKMARK_FILTER);
        else filter = new FilterFavorite();

        setCheckedChip(filter);

        // Create alert dialog instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(binding.getRoot());
        dialog = builder.create();
        return dialog;
    }

    private void setCheckedChip(@NotNull FilterFavorite filter) {
        binding.chipTitle.setChecked(filter.isSortedByTitle());
        binding.chipRating.setChecked(filter.isSortedByRating());
        binding.chipReleaseDate.setChecked(filter.isSortedByReleaseDate());
    }

    @Override
    public void onClick(@NotNull View view) {
        int id = view.getId();
        if (id == binding.btnApply.getId()) {
            filter.setSortedByTitle(binding.chipTitle.isChecked());
            filter.setSortedByRating(binding.chipRating.isChecked());
            filter.setSortedByReleaseDate(binding.chipReleaseDate.isChecked());

            listener.onFilterApplied(filter);
            dialog.dismiss();
        } else if (id == binding.tvReset.getId()) {
            filter = new FilterFavorite();
            setCheckedChip(filter);
        } else if (id == binding.btnCancel.getId() || id == binding.ibClose.getId()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FavoriteFilterDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() +
                    " must implement " + FavoriteFilterDialogListener.class.getSimpleName());
        }
    }

    public interface FavoriteFilterDialogListener {
        void onFilterApplied(FilterFavorite filter);
    }
}
