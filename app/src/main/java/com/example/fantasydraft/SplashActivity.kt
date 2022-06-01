package com.example.fantasydraft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasydraft.databinding.ActivitySplashBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.ktx.Firebase
import com.midina.core_ui.ui.OnStartActivityListener


class SplashActivity : AppCompatActivity(), OnStartActivityListener {

    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setTheme(R.style.Theme_FantasyDraft)
        supportActionBar?.hide()
            //handleDynamicLink()
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

    private fun handleDynamicLink() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener { pdLinkData ->
                if (pdLinkData != null) {
                    val oobCode: String? = pdLinkData.link?.getQueryParameter("oobCode")
                    val fAuth = Firebase.auth
                    if (oobCode != null) {
                        fAuth.checkActionCode(oobCode).addOnSuccessListener { result ->
                            when (result.operation) {
                                ActionCodeResult.VERIFY_EMAIL -> {
                                    fAuth.applyActionCode(oobCode)
                                        .addOnSuccessListener {
                                            Log.i(TAG, "Verified email")
                                        }
                                        .addOnFailureListener { resultCode ->
                                            Log.w(TAG, "Failed to verify email", resultCode)
                                        }
                                }
                                ActionCodeResult.PASSWORD_RESET -> {
                                    Log.d(TAG, "PASSWORD RESET")
                                }
                            }
                        }.addOnFailureListener { result ->
                            Log.w(TAG, "Invalid code sent")
                        }
                    }
                }
            }.addOnFailureListener { ex ->
                Log.w(TAG, "Invalid code sent. $ex")
            }
    }
}