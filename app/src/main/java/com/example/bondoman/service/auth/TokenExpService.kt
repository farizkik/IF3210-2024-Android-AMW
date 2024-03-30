package com.example.bondoman.service.auth

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bondoman.R
import com.example.bondoman.common.response.ResponseContract
import com.example.bondoman.core.repository.auth.token.TokenRepository
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
    }

    enum class Actions {
        START, STOP
    }

    private fun start() {
        val token = preferenceManager.getToken()

        if (!token.isNullOrEmpty()) {
            serviceScope.launch {
                while (true) {
                    Log.d("Token Exp Service", "Start Timer")
                    delay(300000)
                    val response = tokenRepository.getToken("Bearer $token")

                    if (response is ResponseContract.Error) {
                        preferenceManager.removePref()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                }
            }
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Token Check Service Is Active")
            .setContentText("Checking JWT Token Expiration")
            .build()

        startForeground(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}