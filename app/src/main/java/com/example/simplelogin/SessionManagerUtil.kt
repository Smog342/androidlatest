package com.example.simplelogin

import android.content.Context
import java.util.*

object SessionManagerUtil {

    private const val SESSION_PREFERENCES = "SESSION_PREFERENCES"
    private const val SESSION_EXPIRY_DATE = "SESSION_EXPIRY_DATE"

    private const val SESSION_TOKEN = "SESSION_TOKEN"
    private const val SESSION_REFRESH_TOKEN = "SESSION_REFRESH_TOKEN"

    private const val SESSION_LOGIN = "SESSION LOGIN"
    private const val SESSION_PASSWORD = "SESSION PASSWORD"
    private const val SESSION_PHONENUMBER = "SESSION PHONENUMBER"


    fun startUserSession(context: Context, expiresIn: Int){

        val calendar = Calendar.getInstance()
        val userLoggedInTime = calendar.time
        calendar.time = userLoggedInTime

        calendar.add(Calendar.SECOND, expiresIn)
        val expiryTime = calendar.time

        val editor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()
        editor.putLong(SESSION_EXPIRY_DATE, expiryTime.time)
        editor.apply()

    }

    fun isSessionActive(currentTime: Date, context: Context) : Boolean{

        val sessionExpiresAt = Date(getExpiryDateFromPreferences(context)!!)

        return !currentTime.after(sessionExpiresAt)

    }

    fun endUserSession(context: Context){

        clearStoredDate(context)

    }

    fun storeUserToken(context: Context, token: String){

        val tokenEditor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        tokenEditor.putString(SESSION_TOKEN, token)
        tokenEditor.apply()

    }

    fun getUserToken(context: Context) : String?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getString(SESSION_TOKEN,"")

    }

    fun storeRefreshToken(context: Context, token: String){

        val tokenEditor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        tokenEditor.putString(SESSION_REFRESH_TOKEN, token)
        tokenEditor.apply()

    }

    fun getRefreshToken(context: Context) : String?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getString(SESSION_REFRESH_TOKEN,"")

    }

    private fun getExpiryDateFromPreferences(context: Context) : Long?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getLong(SESSION_EXPIRY_DATE,0)

    }

    private fun clearStoredDate(context: Context){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        editor.clear()
        editor.apply()

    }

    fun sessionStoreUserData(context: Context, login: String, password: String, phonenumber: String){

        sessionPutLogin(context,login)
        sessionPutPassword(context, password)
        sessionPutPhonenumber(context, phonenumber)

    }

    fun sessionGetLogin(context: Context) : String?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getString(SESSION_LOGIN,"")

    }

    fun sessionGetPassword(context: Context) : String?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getString(SESSION_PASSWORD,"")

    }

    fun sessionGetPhonenumber(context: Context) : String?{

        return context.getSharedPreferences(SESSION_PREFERENCES,0).getString(SESSION_PHONENUMBER,"")

    }

    private fun sessionPutLogin(context: Context, login: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        editor.putString(SESSION_LOGIN, login)
        editor.apply()

    }
    private fun sessionPutPassword(context: Context, password: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        editor.putString(SESSION_PASSWORD, password)
        editor.apply()

    }
    private fun sessionPutPhonenumber(context: Context, phonenumber: String){

        val editor = context.getSharedPreferences(SESSION_PREFERENCES,0).edit()

        editor.putString(SESSION_PHONENUMBER, phonenumber)
        editor.apply()

    }

}