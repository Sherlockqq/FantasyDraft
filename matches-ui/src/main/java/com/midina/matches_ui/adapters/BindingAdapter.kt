package com.midina.matches_ui.adapters

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.midina.matches_ui.R
import com.midina.matches_ui.fixtures.UiEvent

@BindingAdapter("android:text")
fun TextView.setText(event: UiEvent?) {
    when (event) {
        UiEvent.EmptyState -> this.setText(R.string.empty_state)
        UiEvent.Error -> this.setText(R.string.connection_error)
        UiEvent.Loading -> this.setText(R.string.loading)
        null -> this.setText(R.string.connection_error)
        else -> {}
    }
}

@BindingAdapter("android:visibility")
fun TextView.setVisibility(event: UiEvent?) {
    isVisible = when (event) {
        UiEvent.Error -> true
        UiEvent.EmptyState -> true
        UiEvent.Loading -> true
        is UiEvent.Success -> false
        null -> true
    }
}

@BindingAdapter("android:visibility")
fun ImageView.setVisibility(event: UiEvent?) {
    isVisible = when (event) {
        UiEvent.Error -> true
        UiEvent.EmptyState -> false
        UiEvent.Loading -> false
        is UiEvent.Success -> false
        null -> false
    }
}

@BindingAdapter("android:visibility")
fun ProgressBar.setVisibility(event: UiEvent?) {
    isVisible = when (event) {
        UiEvent.Error -> false
        UiEvent.EmptyState -> false
        UiEvent.Loading -> true
        is UiEvent.Success -> false
        null -> true
    }
}