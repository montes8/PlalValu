package com.tayler.playvalu.repository

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit
import com.valu.uitaycompose.utils.UI_EMPTY

class PreferencesManager @Inject constructor(private val preferences : SharedPreferences){

    fun setValue(key : String, value : String){
        preferences.edit { putString(key, value) }
    }

    fun setValue(key : String, value : Boolean){
        preferences.edit { putBoolean(key, value) }
    }

    fun setValue(key : String, value : Int){
        preferences.edit { putInt(key, value) }
    }

    fun getString(key : String) : String = preferences.getString(key, UI_EMPTY)?: UI_EMPTY

}