package com.tayler.playvalu.repository

import com.tayler.playvalu.usecases.IAppPreferences
import com.tayler.playvalu.utils.PREFERENCE_TOKEN
import javax.inject.Inject

class AppPreferences @Inject constructor(private val preferenceManager : PreferencesManager):
    IAppPreferences {

    override fun saveToken(value : String ) = preferenceManager.setValue(PREFERENCE_TOKEN,value)

    override fun getToken() = preferenceManager.getString(PREFERENCE_TOKEN).isNotEmpty()
}