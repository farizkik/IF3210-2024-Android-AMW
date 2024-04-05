package com.example.bondoman.common
import io.github.cdimascio.dotenv.dotenv


object Constant {
    lateinit var BASE_URL: String
    init {
        try {
            val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }
            BASE_URL = dotenv["BASE_URL"] ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            BASE_URL = ""
        }
    }
}