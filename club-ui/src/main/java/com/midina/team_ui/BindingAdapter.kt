package com.midina.team_ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//@BindingAdapter("android:visibility")
//fun ProgressBar.setVisibility(event: UiEvent?) {
//    isVisible = when (event) {
//        UiEvent.Error -> false
//        UiEvent.Loading -> true
//        UiEvent.EmptyState -> false
//        is UiEvent.Success -> false //TODO IS NOT WORKING
//        null -> true
//    }
//}

@BindingAdapter("android:src")
fun ImageView.setSrc(isAlarm: Boolean) {
    if (isAlarm) {
        this.let {
            Glide.with(this).load(R.drawable.ic_enable_alarm).into(it)
        }
    } else {
        this.let {
            Glide.with(this).load(R.drawable.ic_disable_alarm).into(it)
        }
    }
}

@BindingAdapter("android:visibility")
fun ImageView.setVisibility(event: UiEvent?) {
    isVisible = when (event) {
        UiEvent.Error -> true
        UiEvent.Loading -> false
        UiEvent.EmptyState -> true
        is UiEvent.Success -> true
        null -> false
    }
}

@BindingAdapter("android:visibility")
fun TextView.setVisibility(event: UiEvent?) {
    isVisible = when (event) {
        UiEvent.Error -> true
        UiEvent.Loading -> false
        UiEvent.EmptyState -> true
        is UiEvent.Success -> true
        null -> false
    }
}
