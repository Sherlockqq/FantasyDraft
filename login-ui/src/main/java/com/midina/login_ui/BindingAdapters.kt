package com.midina.login_ui

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:state")
fun TextInputLayout.setState(state: State) {
    when (state) {
        State.Correct ->  {
            this.error = null
            this.isErrorEnabled = false
        }
        State.Default -> {
            this.error = null
            this.isErrorEnabled = false
        }
        State.Undefined -> {
            this.error = "The email is not registered"
        }
        State.Error -> {
            this.error = "The password is incorrect"
        }
    }
}