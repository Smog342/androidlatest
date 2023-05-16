package com.example.simplelogin

import android.content.Context
import java.util.*

object SessionManagerUtil {

    const val SESSION_PREFERENCES = "SESSION_PREFERENCES"
    const val SESSION_EXPIRY_TIME = "SESSION_EXPIRY_TIME"
    const val SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN"
    const val SESSION_REFRESH_TOKEN = "SESSION_REFRESH_TOKEN"
    const val SERVER_REFRESH_TOKEN = "SERVER_REFRESH_TOKEN"

    fun startUserSession(context: Context, expiresIn: Int){

        val calendar = Calendar.getInstance()
        val userLoggedInTime = calendar.time
        calendar.time = userLoggedInTime
        calendar.add(Calendar.SECOND, expiresIn)
        val expiryTime = calendar.time
        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.putLong(SESSION_EXPIRY_TIME, expiryTime.time)
        editor.apply()

    }

    fun isSessionActive(currentTime: Date,context: Context) : Boolean {

        val sessionExpiresAt = Date(getExpiryDateFromPreferences(context)!!)
        return !currentTime.after(sessionExpiresAt)

    }

    fun endUserSession(context: Context){

        clearStoredData(context)

    }

    fun storeAccessionToken(context: Context, token: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.putString(SESSION_ACCESS_TOKEN, token)
        editor.apply()

    }

    fun storeRefreshToken(context: Context, token: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.putString(SESSION_REFRESH_TOKEN, token)
        editor.apply()

    }

    fun storeRefreshTokenAtServer(context: Context, token: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.putString(SERVER_REFRESH_TOKEN, token)
        editor.apply()

    }

    private fun getExpiryDateFromPreferences(context: Context) : Long? {
        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getLong(SESSION_EXPIRY_TIME, 0)
    }

    private fun clearStoredData(context: Context) {
        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.clear()
        editor.apply()
    }

    fun getRefreshToken(context: Context) : String? {

        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SESSION_REFRESH_TOKEN, "")

    }

    fun getRefreshTokenFromServer(context: Context) : String? {

        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SERVER_REFRESH_TOKEN, "")

    }

    fun getAccessToken(context: Context) : String? {

        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SESSION_ACCESS_TOKEN, "")

    }

}