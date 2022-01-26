package com.example.fantasydraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.fantasydraft.databinding.ActivitySplashBinding
import com.midina.core_ui.ui.OnStartActivityListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity(), OnStartActivityListener {

    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setTheme(R.style.Theme_FantasyDraft)
        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun startMainActivity(team: String) {
        val intent = Intent(this, MainActivity::class.java)
            .putExtra("Team", team)
        startActivity(intent)
        finish()
    }
}