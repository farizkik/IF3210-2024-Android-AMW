package com.example.bondoman.service.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.bondoman.common.response.ResponseContract
import com.example.bondoman.core.repository.auth.token.TokenRepository
import com.example.bondoman.network.ConnectivityObserver
import com.example.bondoman.network.NetworkConnectivityObserver
import com.example.bondoman.share_preference.PreferenceManager
import com.example.bondoman.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TokenExpService : Service() {

    private val notificationId = 123
    private val channelId = "token_check_running_channel"

    private lateinit var preferenceManager: PreferenceManager

    private lateinit var tokenRepository: TokenRepository
    private lateinit var connectivityObserver: ConnectivityObserver

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + job)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        preferenceManager = PreferenceManager(this)
        tokenRepository = TokenRepository()
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
    }

    enum class Actions {
        START, STOP
    }

    private fun start() {
        val token = preferenceManager.getToken()

        Log.d("Token Exp Service", "Start")

        if (!token.isNullOrEmpty()) {
            serviceScope.launch {

                val isLogin = preferenceManager.isLogin()

                if(isLogin) {
                    Log.d("Token Exp Service", "Start Timer")
                    delay(300000)
                    if (connectivityObserver.isConnected()) {
                        redirectionChecking(token)
                    } else {
                        Log.d("Token Exp Service", "No network")
                        connectivityObserver.observe().collect { status ->
                            if (status == ConnectivityObserver.Status.Available) {
                                redirectionChecking(token)
                            }
                        }
                    }
                }
            }
        }

//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Token Check Service Is Active")
//            .setContentText("Checking JWT Token Expiration")
//            .build()
//
//        startForeground(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    private suspend fun redirectionChecking(token: String) {
        val response = tokenRepository.getToken("Bearer $token")

        if (response is ResponseContract.Error) {
            preferenceManager.removePref()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}