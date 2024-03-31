package com.example.bondoman.common
import android.util.Log
import io.github.cdimascio.dotenv.dotenv


object Constant {
    lateinit var BASE_URL: String
    init {
        try {
            Log.d("Constant", "this is message")
            val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }
            Log.d("Constant", "this is dotenv $dotenv")
            BASE_URL = dotenv["BASE_URL"] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            BASE_URL = ""
        }
    }
}