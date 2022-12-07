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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
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
                //slideUp.startDelay = 800L
                slideUp.start()
            }
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.bottom_notes,
                R.id.bottom_calendar,
                R.id.bottom_reminders,
                R.id.bottom_settings
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(
                    R.id.bottom_notes,
                    R.id.bottom_calendar,
                    R.id.bottom_reminders,
                    R.id.bottom_settings
                )
            ) {
                binding.bottomNavigationView.visibility = View.VISIBLE

            } else {
                binding.bottomNavigationView.visibility = View.GONE

            }
            val isTopLevelDestination =
                appBarConfiguration.topLevelDestinations.contains(destination.id)
            if (!isTopLevelDestination) {
                binding.mainToolbar.setNavigationIcon(
                    R.drawable.ic_back_arrow
                )
                binding.mainToolbar.setNavigationIconTint(
                    ContextCompat.getColor(
                        this,
                        R.color.back_arrow_tint
                    )
                )
                if (isDarkTheme()) {
                    setNavigationColor(
                        ContextCompat.getColor(
                            this,
                            R.color.onyx
                        )
                    )
                }
            } else {
                if (isDarkTheme()) {
                    setNavigationColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray
                        )
                    )
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
        } else {
            requestPermissions(READ_EXTERNAL_STORAGE)
        }
    }

}