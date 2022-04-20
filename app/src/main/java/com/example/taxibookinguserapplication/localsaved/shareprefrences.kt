package com.rehablab.localsaved

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.taxibookinguserapplication.Login.LoginActivity


class shareprefrences(val context: Context) {


    private val PREF_NAME = "SHARED_PREFERENCE"
    private var preferences: SharedPreferences? = null

    fun setStringPreference( KEY: String,
                            value: String) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putString(KEY, "" + value)
        editor.apply()
    }


    fun setSetPrefrence( KEY: String,
                             value:Set<String>) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
       editor.putStringSet(KEY,value)
        editor.apply()
    }

    fun isSession( KEY: String,
                  value: Boolean) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putBoolean(KEY, value)
        editor.apply()
    }

    fun isFlag(context: Context, KEY: String, value: Boolean) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putBoolean(KEY, value)
        editor.apply()
    }

    fun isGetFlag(context: Context, KEY: String): Boolean {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences!!.getBoolean(KEY, false)
    }

    fun isGetSession( KEY: String): Boolean {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences!!.getBoolean(KEY, false)
    }

    fun getStringPreference( KEY: String): String? {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences!!.getString(KEY, "")
    }


    fun clearAllPreferences() {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        preferences!!.edit().clear().apply()
        context.startActivity(Intent(context,LoginActivity::class.java))


    }

    fun setlanguagesetting(value:String){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putString("Language", value)
        editor.apply()

    }

    fun getlanguage(): String?{
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences!!.getString("Language", "EN")

    }

}



