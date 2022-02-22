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

    fun getTeamTheme(): Int {
        val sPref =  this.activity?.getSharedPreferences(
            "SplashActivity",
            AppCompatActivity.MODE_PRIVATE
        )
        val team = sPref?.getString(SAVED_TEAM, "")
        if (team != null) {
            when (team) {
                "Динамо Киев" -> return R.style.Theme_DynamoNoAction
            }
        }
        return R.style.Theme_FantasyDraft
    }

    fun getTeamActionTheme(): Int {
        val sPref =  this.activity?.getSharedPreferences(
            "SplashActivity",
            AppCompatActivity.MODE_PRIVATE
        )
        val team = sPref?.getString(SAVED_TEAM, "")
        if (team != null) {
            when (team) {
                "Динамо Киев" -> return R.style.Theme_Dynamo
            }
        }
        return R.style.Theme_FantasyDraft
    }

    companion object {
        const val SAVED_TEAM = "TEAM"
    }
}