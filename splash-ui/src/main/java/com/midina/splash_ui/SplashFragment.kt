package com.midina.splash_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnStartActivityListener
import com.midina.splash_ui.databinding.FragmentSplashBinding
import kotlinx.coroutines.flow.collect

const val TAG = "SplashFragment"

class SplashFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_splash

    val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    private lateinit var binding: FragmentSplashBinding
    private var listenerOn: OnStartActivityListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }
        return binding.root
    }

    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> onSuccess(event.season)
            is UiEvent.Error -> onError()
            is UiEvent.Loading -> onLoading()
            is UiEvent.EmptyState -> onEmptyState()
        }
    }

    private fun onSuccess(season: Int) {
        checkSeason(season)
    }

    private fun onError() {
        Log.d(TAG, "onError")
    }

    private fun onLoading() {
        Log.d(TAG, "onLoading")
        hidingAnimation()
    }

    private fun onEmptyState() {
        Log.d(TAG, "onEmptyState")
    }

    private fun hidingAnimation() {
        Log.d(TAG, "hidingAnimation: ")
        val anim = AnimationUtils.loadAnimation(context, R.anim.to_invisible)
        binding.ivUplLogo.startAnimation(anim)
    }

    private fun checkSeason(season: Int) {
        val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val sPrefSeason = sPref?.getInt(SAVED_SEASON, 0)
        val teamId = sPref?.getInt(FAVOURITE_TEAM_ID, 0)
        val teamLogo = sPref?.getString(FAVOURITE_TEAM_LOGO, "")

        if ( sPrefSeason == 0 || season != sPrefSeason
            || teamId == 0 || teamLogo.isNullOrEmpty()
        ) {
            val ed = sPref?.edit()
            ed?.putInt(SAVED_SEASON, season)
            ed?.apply()

            navigateToFavourite(season)
        } else {
            teamId?.let { id ->
                navigateToMainActivity(id, teamLogo.toString())
            }
        }
    }

    private fun navigateToFavourite(season: Int) {
        val bundle = Bundle()
        bundle.putInt(SAVED_SEASON, season)
        findNavController().navigate(R.id.action_favourite_navigation, bundle)
    }

    private fun navigateToMainActivity(teamId: Int, teamLogo: String) {
        if (context is OnStartActivityListener) {
            listenerOn = context as OnStartActivityListener
            listenerOn?.startMainActivity(teamId, teamLogo)
        }
    }
}