package com.example.bondoman

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.databinding.ActivityMainBinding
import com.example.bondoman.receiver.MyBroadcastListener
import com.example.bondoman.receiver.MyBroadcastReceiver

class MainActivity : AppCompatActivity(), MyBroadcastListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_transactions, R.id.navigation_notifications, R.id.navigation_settings, R.id.transactionFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onBroadcastReceived(value: String?) {
        Log.d("adsadas", "asdasd")
    }
}