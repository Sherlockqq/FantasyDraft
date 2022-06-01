package com.midina.draft_ui

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter

@BindingAdapter("app:sign")
fun AppCompatButton.setSign(event: SigningUiEvent?) {
    when (event) {
        is SigningUiEvent.OnVerified -> {
            this.setBackgroundResource(R.drawable.ic_sign_out)
        }
        SigningUiEvent.OnNotSignIn -> {
            this.setBackgroundResource(R.drawable.ic_signin)
        }
        is SigningUiEvent.OnNotVerified -> {
            this.setBackgroundResource(R.drawable.ic_sign_out)
        }
        else -> {
            this.setBackgroundResource(R.drawable.ic_signin)
        }
    }
}

@BindingAdapter("app:action")
fun AppCompatButton.setAction(event: SigningUiEvent?) {
    when (event) {
        is SigningUiEvent.OnVerified -> {
            this.setBackgroundResource(R.drawable.ic_draft)
        }
        is SigningUiEvent.OnNotSignIn -> {
            this.setBackgroundResource(R.drawable.ic_registration)
        }
        is SigningUiEvent.OnNotVerified -> {
            this.setBackgroundResource(R.drawable.ic_verify)
        }
        else -> {
            this.setBackgroundResource(R.drawable.ic_registration)
        }
    }
}