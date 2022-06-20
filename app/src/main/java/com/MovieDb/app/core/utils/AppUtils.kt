package com.MovieDb.app.core.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.MovieDb.app.R
import com.MovieDb.app.core.data.source.local.entity.FavoriteEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object AppUtils {
    @JvmStatic
    fun equalsFavoriteObjects(o1: FavoriteEntity, o2: FavoriteEntity): Boolean {
        try {
            return o1.id.equals(o2.id) && o1.type == o2.type && o1.title == o2.title && o1.poster == o2.poster && o1.scoreAverage == o2.scoreAverage && o1.startDate == o2.startDate && o1.genres!!.size == o2.genres!!.size
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return false
    }

    @JvmStatic
    fun loadImage(imageView: ImageView, source: Any, size: String) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(Constants.BASE_URL_TMDB_IMAGE + size + source)
            .apply(myGlideOptions())
            .centerCrop()
            .into(imageView)
    }

    private fun myGlideOptions(): RequestOptions {
        return RequestOptions.placeholderOf(R.drawable.ic_placeholder_96)
            .error(R.drawable.ic_placeholder_96)
    }

    @JvmStatic
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}