package ru.artsto.centralbank.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{
    @Provides
    @Singleton
    fun provideContext(app : Application) : Context {
        return app.applicationContext
    }
}