package com.example.fantasydraft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasydraft.databinding.ActivitySplashBinding
import com.midina.core_ui.ui.BaseFragment.Companion.TEAM_ID
import com.midina.core_ui.ui.BaseFragment.Companion.FAVOURITE_TEAM_LOGO
import com.midina.core_ui.ui.OnStartActivityListener

class SplashActivity : AppCompatActivity(), OnStartActivityListener {

    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivitySplashBinding.inflate(layoutInflater)

//        setTheme(R.style.Theme_FantasyDraft)
//        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun startMainActivity(teamId: Int, teamLogo: String) {
        val intent = Intent(this, MainActivity::class.java)
            .putExtra(TEAM_ID, teamId)
            .putExtra(FAVOURITE_TEAM_LOGO, teamLogo)

        startActivity(intent)
        finish()
    }
}