package com.midina.core_ui.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.midina.core_ui.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    abstract val layoutId: Int

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    companion object {
        const val SEASON = "SEASON"
        const val TEAM_ID = "FAVOURITE_TEAM_ID"
        const val FAVOURITE_TEAM_LOGO = "FAVOURITE_TEAM_LOGO"
    }
}