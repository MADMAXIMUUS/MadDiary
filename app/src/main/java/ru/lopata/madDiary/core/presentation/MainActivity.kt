package ru.lopata.madDiary.core.presentation

import android.Manifest.permission.*
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
import ru.lopata.madDiary.core.util.requestPermissions
import ru.lopata.madDiary.databinding.ActivityMainBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.Event.Types.Companion.toTypesEnum
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

        WindowCompat.setDecorFitsSystemWindows(window, false)

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

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationRoot) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.rootView.updatePadding(top = insets.top)
            view.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(
                    R.id.bottom_notes,
                    R.id.bottom_calendar,
                    R.id.bottom_reminders,
                    R.id.bottom_settings,
                    R.id.bottomSheetChooseReminderTypeFragment
                )
            ) {
                binding.bottomNavigationRoot.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationRoot.visibility = View.GONE
            }
        }

        val eventId = intent.getIntExtra("eventId", -1)
        val eventType = intent.getStringExtra("eventType")?.toTypesEnum()

        if (eventId != -1) {
            val action = when (eventType) {
                Event.Types.EVENT -> {
                    ListEventFragmentDirections.actionBottomRemindersToViewEventFragment(
                        eventId = eventId, chapter = -1, chapters = -1
                    )
                }
                Event.Types.TASK -> {
                    ListEventFragmentDirections.actionBottomRemindersToViewTaskFragment(eventId = eventId)
                }
                Event.Types.REMINDER -> {
                    ListEventFragmentDirections.actionBottomRemindersToViewReminderFragment(eventId = eventId)
                }
                null -> {
                    null
                }
            }
            if (action != null) navController.navigate(action)
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
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager = this.getSystemService(NotificationManager::class.java)

        val group = NotificationChannelGroup(
            "eventTaskReminderGroup", getString(R.string.event_chanel_group)
        )
        notificationManager.createNotificationChannelGroup(group)

        if (notificationManager.getNotificationChannel("eventAlarm") == null) {
            val eventChannel = NotificationChannel(
                "eventAlarm",
                getString(R.string.event_chanel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.event_description)
                this.group = "eventTaskReminderGroup"
            }
            notificationManager.createNotificationChannel(eventChannel)
        }

        if (notificationManager.getNotificationChannel("reminderAlarm") == null) {
            val reminderChannel = NotificationChannel(
                "reminderAlarm",
                getString(R.string.reminder_chanel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.reminder_description)
                this.group = "eventTaskReminderGroup"
            }
            notificationManager.createNotificationChannel(reminderChannel)
        }

        if (notificationManager.getNotificationChannel("taskAlarm") == null) {
            val taskChannel = NotificationChannel(
                "taskAlarm",
                getString(R.string.task_chanel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.task_description)
                this.group = "eventTaskReminderGroup"
            }
            notificationManager.createNotificationChannel(taskChannel)
        }

        if (notificationManager.getNotificationChannel("progress_channel") == null) {
            val notificationChannel = NotificationChannel(
                "progress_channel",
                getString(R.string.attachment_chanel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.attachment_description)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}