package com.example.bondoman.common
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()

object Constant {
    val BASE_URL = dotenv["BASE_URL"]
}