package com.example.parquerufinotamayo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.parquerufinotamayo.model.entities.JwtToken

class LoginUtils {
    companion object {
        val BASE_URL = "http://10.0.2.2:3000/"

        private const val TOKEN_PREFS = "tokenPrefs"
        private const val TOKEN_KEY = "tokenKey"

        fun getToken(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val token = sharedPreferences.getString(TOKEN_KEY, "WRONG_TOKEN")
            Log.i("Utils", "Token is ${token!!}")
            return token!!
        }

        fun saveToken(token: JwtToken, context:Context){
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString(TOKEN_KEY, token.token)
            editor.commit()
        }

        fun isUserLoggedIn(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            return sharedPreferences.contains(TOKEN_KEY)
        }
    }
}