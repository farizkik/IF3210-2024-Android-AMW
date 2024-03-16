package com.example.bondoman.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver(private val listener: MyBroadcastListener) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        listener.onBroadcastReceived(intent.action.toString())
    }
}