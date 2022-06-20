package com.MovieDb.app.core.utils

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerHelper {
    private val shimmer: ShimmerFrameLayout
    private val contents: View
    private var emptyState: View? = null

    constructor(shimmer: ShimmerFrameLayout, contents: View) {
        this.shimmer = shimmer
        this.contents = contents
    }

    constructor(shimmer: ShimmerFrameLayout, contents: View, emptyState: View?) {
        this.shimmer = shimmer
        this.contents = contents
        this.emptyState = emptyState
    }

    fun show() {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        contents.visibility = View.INVISIBLE
        if (emptyState != null) emptyState!!.visibility = View.INVISIBLE
    }

    fun hide(isEmpty: Boolean) {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        if (isEmpty) {
            if (emptyState != null) {
                emptyState!!.visibility = View.VISIBLE
            }
        } else {
            contents.visibility = View.VISIBLE
        }
    }
}