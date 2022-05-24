package com.midina.match_ui

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:visibility")
fun ImageView.setVisibility(event: UiMatchEvent?) {
    isVisible = when (event) {
        UiMatchEvent.Error -> true
        UiMatchEvent.Loading -> false
        is UiMatchEvent.Success -> true
        null -> false
    }
}

@BindingAdapter("android:visibility")
fun TextView.setVisibility(event: UiMatchEvent?) {
    isVisible = when (event) {
        UiMatchEvent.Error -> true
        UiMatchEvent.Loading -> false
        is UiMatchEvent.Success -> true
        null -> false
    }
}

@BindingAdapter("android:visibility")
fun ProgressBar.setVisibility(event: UiMatchEvent?) {
    isVisible = when (event) {
        UiMatchEvent.Error -> false
        UiMatchEvent.Loading -> true
        is UiMatchEvent.Success -> false
        null -> true
    }
}