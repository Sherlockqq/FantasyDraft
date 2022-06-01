package com.midina.registration_ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:state")
fun ImageView.setState(state: State?) {
    when (state) {
        State.DEFAULT -> {
            this.let {
                Glide
                    .with(this)
                    .load(R.drawable.ic_default)
                    .into(it)
            }
        }
        State.ERROR -> {
            this.let {
                Glide
                    .with(this)
                    .load(R.drawable.ic_error)
                    .into(it)
            }
        }
        State.CORRECT -> {
            this.let {
                Glide
                    .with(this)
                    .load(R.drawable.ic_success)
                    .into(it)
            }
        }
        else -> {
            this.let {
                Glide
                    .with(this)
                    .load(R.drawable.ic_default)
                    .into(it)
            }
        }
    }
}

@BindingAdapter("app:state")
fun TextView.setState(state: State?) {
    when (state) {
        State.DEFAULT -> {
            this.isGone = true
        }
        State.ERROR -> {
            this.isVisible = true
        }
        State.CORRECT -> {
            this.isGone = true
        }
        else -> {
            this.isGone = true
        }
    }
}