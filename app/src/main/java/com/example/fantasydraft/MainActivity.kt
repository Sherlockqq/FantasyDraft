package com.example.fantasydraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fantasydraft.databinding.ActivityMainBinding
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.midina.matches_ui.OnArrowClickListener
import com.midina.matches_ui.fixtures.FixturesFragment

class MainActivity : AppCompatActivity(), OnBottomNavHideListener, OnArrowClickListener {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onResume() {
        super.onResume()
        onShowBottomNavView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fixturesFragment, R.id.draftFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onHideBottomNavView() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        binding.bottomNavigation.startAnimation(anim)
        binding.bottomNavigation.isVisible = false
    }

    override fun onShowBottomNavView() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        binding.bottomNavigation.startAnimation(anim)
        binding.bottomNavigation.isVisible = true
    }

    override fun onArrowNextClicked() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as FixturesFragment
        fragment.nextPage()
    }

    override fun onArrowBackClicked() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as FixturesFragment
        fragment.previousPage()
    }
}