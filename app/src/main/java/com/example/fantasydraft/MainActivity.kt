package com.example.fantasydraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fantasydraft.databinding.ActivityMainBinding
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnBottomNavHideListener {

@Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
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

    override fun onHide() {
        binding.bottomNavigation.isVisible = false
    }

    override fun onShow() {
        binding.bottomNavigation.isVisible = true
    }

}