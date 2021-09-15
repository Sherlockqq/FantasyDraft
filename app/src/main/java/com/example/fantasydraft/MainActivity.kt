package com.example.fantasydraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController


import com.example.fantasydraft.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.internal.ContextUtils.getActivity


//TODO Remove BNV When it needed
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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

        this.getSupportFragmentManager().addOnBackStackChangedListener{
            val f: Fragment? =
                this.getSupportFragmentManager().findFragmentById(R.id.registrationFragment)
            if(f == getVisibleFragment()){
                binding.bottomNavigation.isGone = true
            }else{
                binding.bottomNavigation.isVisible = true
            }
        }


    }
    private fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this.getSupportFragmentManager();
        val fragments: List<Fragment> = fragmentManager.getFragments();
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null
    }
}