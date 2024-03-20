package com.example.bondoman

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.databinding.ActivityMainBinding
import com.example.bondoman.receiver.MyBroadcastListener
import com.example.bondoman.receiver.MyBroadcastReceiver
import com.example.bondoman.service.ConnectivityObserver
import com.example.bondoman.service.NetworkConnectivityObserver
import com.example.bondoman.ui.network.NetworkOfflineFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MyBroadcastListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: BroadcastReceiver
    private lateinit var  connectivityObserver: ConnectivityObserver
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

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

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            observeConnectivity()
        }
    }

    private fun observeConnectivity() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
                    showNoInternetPopUp()
                } else {
                    hideNoInternetPopup()
                }
            }
        }
    }

    private fun showNoInternetPopUp() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun hideNoInternetPopup() {
        alertDialog?.dismiss()
        alertDialog = null
    }

    override fun onBroadcastReceived(value: String?) {
        Log.d("adsadas", "asdasd")
    }
}