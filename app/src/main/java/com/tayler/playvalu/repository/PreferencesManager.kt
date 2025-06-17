package com.tayler.playvalu.repository

import android.content.SharedPreferences
import com.tayler.playvalu.utils.EMPTY
import javax.inject.Inject
import androidx.core.content.edit

class PreferencesManager @Inject constructor(private val preferences : SharedPreferences){

    fun setValue(key : String, value : String){
        preferences.edit() { putString(key, value) }
    }

    fun setValue(key : String, value : Boolean){
        preferences.edit() { putBoolean(key, value) }
    }

    fun setValue(key : String, value : Int){
        preferences.edit() { putInt(key, value) }
    }

    fun getString(key : String) : String = preferences.getString(key, EMPTY)?: EMPTY

}