package com.example.parquerufinotamayo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.parquerufinotamayo.model.entities.JwtToken

class LoginUtils {
    companion object {
        val BASE_URL = "http://100.24.228.237:10017/"

        private const val TOKEN_PREFS = "tokenPrefs"
        private const val TOKEN_KEY = "tokenKey"
        private const val USER_NAME = "anyone"
        private const val USER_TYPE = "user"

        fun getToken(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val token = sharedPreferences.getString(TOKEN_KEY, "WRONG_TOKEN")
            Log.i("Utils", "Token is ${token!!}")
            return token!!
        }

        fun getUser(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val user = sharedPreferences.getString(USER_NAME, "WRONG_USER")
            Log.i("Utils", "User is ${user!!}")
            return user!!
        }

        fun getUserType(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val type = sharedPreferences.getString(USER_TYPE, "WRONG_TYPE")
            Log.i("Utils", "Usertype is ${type!!}")
            return type!!
        }

        fun saveToken(token: JwtToken, context:Context){
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString(TOKEN_KEY, token.token)
            editor.apply()
            editor.putString(USER_NAME, token.username)
            editor.apply()
            editor.putString(USER_TYPE, token.type)
            editor.apply()
        }

        fun deleteInfo(context:Context){
            val sharedPreferences = context.getSharedPreferences(
                TOKEN_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
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