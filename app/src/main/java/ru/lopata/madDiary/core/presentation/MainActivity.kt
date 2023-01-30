package ru.lopata.madDiary.core.presentation

import android.Manifest.permission.*
import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.isDarkTheme
import ru.lopata.madDiary.core.util.requestPermissions
import ru.lopata.madDiary.core.util.setNavigationColor
import ru.lopata.madDiary.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L
                slideUp.doOnEnd {
                    splashScreenView.remove()
                }
                slideUp.start()
            }
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(
                    R.id.bottom_notes,
                    R.id.bottom_calendar,
                    R.id.bottom_reminders,
                    R.id.bottom_settings
                )
            ) {
                binding.bottomNavigationView.visibility = View.VISIBLE
                if (isDarkTheme()) {
                    setNavigationColor(ContextCompat.getColor(this, R.color.dark_gray))
                }
            } else {
                binding.bottomNavigationView.visibility = View.GONE
                if (isDarkTheme()) {
                    setNavigationColor(ContextCompat.getColor(this, R.color.onyx))
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        CoroutineScope(Dispatchers.Main).launch {
            delay(400L)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES, POST_NOTIFICATIONS)
            } else {
                requestPermissions(READ_EXTERNAL_STORAGE)
            }
        }
    }
}