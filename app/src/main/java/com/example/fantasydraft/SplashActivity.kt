package com.example.fantasydraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.fantasydraft.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setTheme(R.style.Theme_FantasyDraft)
        supportActionBar?.hide()
        setContentView(binding.root)
        hidingAnimation()
        startMainActivity(2800)
    }

    private fun hidingAnimation() {
        Log.d(TAG, "hidingAnimation: ")
        val anim = AnimationUtils.loadAnimation(this, R.anim.to_invisible)
        binding.ivUplLogo.startAnimation(anim)
    }

    private fun startMainActivity(time: Int) {
        Log.d(TAG, "startMainActivity: ")
        val intent = Intent(this, MainActivity::class.java)
        lifecycleScope.launch {
            delay(time.toLong())
            binding.ivUplLogo.isVisible = false
            startActivity(intent)
            finish()
        }
    }
}