package ru.madmax.madnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madnotes.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        val navController = this.findNavController(R.id.app_navigation)
        binding.bottomNavigationView.setupWithNavController(navController = navController)

        /*binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_notes -> {

                }
                R.id.bottom_categories -> {

                }
                R.id.bottom_add -> {

                }
                R.id.bottom_reminders -> {

                }
                R.id.bottom_settings -> {

                }
                else -> false
            }
        }*/

    }
}