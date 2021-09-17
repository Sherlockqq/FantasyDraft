package com.example.fantasydraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.fantasydraft.registration.OnBottomNavHideListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.internal.ContextUtils.getActivity


//TODO Remove BNV When it needed
class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener,
    OnBottomNavHideListener {

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

        supportFragmentManager.addOnBackStackChangedListener(this)
//        supportFragmentManager.addOnBackStackChangedListener{
//            val f: Fragment? =
//                this.supportFragmentManager.findFragmentById(R.id.registrationFragment)
//            if(f == getVisibleFragment()){
//                binding.bottomNavigation.isGone = true
//            }else{
//                binding.bottomNavigation.isVisible = true
//            }
//        }


    }
//    var fragManager = this.supportFragmentManager
//    var count = this.supportFragmentManager.backStackEntryCount
//    var frag = fragManager.fragments[if (count > 0) count - 1 else count]



    private fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
            for (fragment in fragments) {
                fragment?.let {

                }
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        return null
    }

    override fun onBackStackChanged() {
        Log.e("MainActivity", "onBackStackChanged: ", )
    }

    override fun onHide() {
        binding.bottomNavigation.isVisible = false
    }

    override fun onShow() {
        binding.bottomNavigation.isVisible = true
    }

}