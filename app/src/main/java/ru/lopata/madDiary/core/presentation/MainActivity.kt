package ru.lopata.madDiary.core.presentation

import android.Manifest.permission.*
import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import ru.lopata.madDiary.core.presentation.settings.ThemeEnum
import ru.lopata.madDiary.core.presentation.settings.ThemeEnum.Companion.toThemeEnum
import ru.lopata.madDiary.core.util.THEME
import ru.lopata.madDiary.core.util.isDarkTheme
import ru.lopata.madDiary.core.util.requestPermissions
import ru.lopata.madDiary.core.util.setNavigationColor
import ru.lopata.madDiary.databinding.ActivityMainBinding
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventFragmentDirections
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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

        val theme = sharedPreferences.getString(THEME, ThemeEnum.SYSTEM.toString()).toString()
        when (theme.toThemeEnum()) {
            ThemeEnum.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            ThemeEnum.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            ThemeEnum.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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

        val eventId = intent.getIntExtra("eventId", -1)

        if (eventId != -1) {
            val action = ListEventFragmentDirections
                .actionBottomRemindersToViewEventFragment(
                    eventId = eventId,
                    chapter = -1,
                    chapters = -1
                )
            navController.navigate(action)
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