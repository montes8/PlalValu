package com.tayler.playvalu.repository.di

import com.tayler.playvalu.repository.AppPreferences
import com.tayler.playvalu.usecases.IAppPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigMyModule{

    @Singleton
    @Binds
    abstract fun provideIAppPreferences(
        appPreferences: AppPreferences
    ): IAppPreferences
}
