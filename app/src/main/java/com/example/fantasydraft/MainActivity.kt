package com.example.fantasydraft

import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fantasydraft.databinding.ActivityMainBinding
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.midina.core_ui.ui.OnBottomNavItemSelectListener


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    OnBottomNavHideListener, OnBottomNavItemSelectListener {

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

        val navView: BottomNavigationView = binding.bottomNavigation.apply {
            itemIconTintList = null
        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fixturesFragment, R.id.draftFragment, R.id.clubFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        openBundle()
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

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val team = extras.getString("Team")
            if (!team.isNullOrEmpty()) {
                val menu: Menu = binding.bottomNavigation.menu
                val item = menu.findItem(R.id.club_navigation)
                item.setIcon(getImage(team))
            }
        }
    }

    private fun getImage(team: String): Int {
        when (team) {
            "Львов" -> return R.drawable.lviv_logo
            "Верес" -> return R.drawable.veres_logo
            "Шахтер Донецк" -> return R.drawable.shakhtar_logo
            "Металлист 1925" -> return R.drawable.metallist25_logo
            "Десна" -> return R.drawable.desna_logo
            "Заря" -> return R.drawable.zarya_logo
            "Ворскла" -> return R.drawable.vorskla_logo
            "Динамо Киев" -> return R.drawable.dynamo_logo
            "Мариуполь" -> return R.drawable.mariupol_logo
            "Колос К" -> return R.drawable.kolos_logo
            "Ингулец" -> return R.drawable.ingulets_logo
            "Рух Львов" -> return R.drawable.rukh_logo
            "Черноморец" -> return R.drawable.chornomorets_logo
            "Александрия" -> return R.drawable.oleksandriya_logo
            "Днепр-1" -> return R.drawable.dnipro1_logo
            "Минай" -> return R.drawable.minaj_logo
        }
        return R.drawable.connection_error
    }

    override fun highlightItem(itemId: Int) {
        val menu: Menu = binding.bottomNavigation.menu
        when (itemId) {
            R.id.fixtures_navigation -> {
                val item = menu.findItem(R.id.fixtures_navigation)
                //TODO setIcon
            }
            R.id.draft_navigation -> {
                val item = menu.findItem(R.id.draft_navigation)
            }
            R.id.club_navigation -> {
                val item = menu.findItem(R.id.club_navigation)
            }
        }
    }

}