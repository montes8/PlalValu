package com.tayler.playvalu.usecases

interface IAppPreferences {
    fun saveToken(value : String )

    fun getToken() : Boolean

}