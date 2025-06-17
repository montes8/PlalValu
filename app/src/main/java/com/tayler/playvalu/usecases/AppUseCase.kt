package com.tayler.playvalu.usecases

import com.tayler.playvalu.utils.EMPTY
import javax.inject.Inject

class AppUseCase @Inject constructor(private val iAppPreference : IAppPreferences) {
    fun logout() = iAppPreference.saveToken(EMPTY)
    fun getToken() = iAppPreference.getToken()

}