package com.tayler.playvalu.usecases


import com.valu.uitaycompose.utils.UI_EMPTY
import javax.inject.Inject

class AppUseCase @Inject constructor(private val iAppPreference : IAppPreferences) {
    fun logout() = iAppPreference.saveToken(UI_EMPTY)
    fun getToken() = iAppPreference.getToken()

}