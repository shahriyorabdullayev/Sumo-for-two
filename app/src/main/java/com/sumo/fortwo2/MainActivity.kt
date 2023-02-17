package com.sumo.fortwo2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sumo.fortwo2.common.Constants.SOUND
import com.sumo.fortwo2.common.SharedPref
import com.sumo.fortwo2.service.SoundService
import dev.b3nedikt.app_locale.AppLocale

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (SharedPref(this).getBoolean(SOUND)) {
            startService(Intent(this, SoundService::class.java))
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.gameFragment -> {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.game_status)
                }
                R.id.resultFragment -> {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.game_status)
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.menu_status)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, SoundService::class.java))

    }

    private val appCompatDelegate: AppCompatDelegate by lazy {
        ViewPumpAppCompatDelegate(
            baseDelegate = super.getDelegate(),
            baseContext = this,
            wrapContext = AppLocale::wrap
        )
    }

    override fun getDelegate(): AppCompatDelegate {
        return appCompatDelegate
    }
}