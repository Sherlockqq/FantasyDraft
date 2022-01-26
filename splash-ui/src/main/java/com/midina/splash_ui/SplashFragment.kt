package com.midina.splash_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnStartActivityListener
import com.midina.splash_ui.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG = "SplashFragment"
const val ANIMATION_DURATION = 3000L

class SplashFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_splash

    private var isSigned = false
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

        hidingAnimation()
        chooseFragment()

        return binding.root
    }

    private fun hidingAnimation() {
        Log.d(TAG, "hidingAnimation: ")
        val anim = AnimationUtils.loadAnimation(context, R.anim.to_invisible)
        binding.ivUplLogo.startAnimation(anim)
    }

    private fun chooseFragment() {
        val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val team = sPref?.getString(SAVED_TEAM, "")
        isSigned = !team.isNullOrEmpty()
        navigateToFragment(team)
    }

    private fun navigateToFragment(team: String?) {
        lifecycleScope.launch {
            delay(ANIMATION_DURATION)
            binding.ivUplLogo.isVisible = false
            if (isSigned) {
                if (team != null) {
                    navigateToMainActivity(team)
                }
            } else {
                findNavController().navigate(R.id.action_favourite_navigation)
            }
        }
    }

    private fun navigateToMainActivity(team: String) {
        if (context is OnStartActivityListener) {
            listenerOn = context as OnStartActivityListener
            listenerOn?.startMainActivity(team)
        }
    }
}