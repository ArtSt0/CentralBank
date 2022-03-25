package ru.artsto.centralbank

import android.app.Application
import ru.artsto.centralbank.di.components.AppComponent
import ru.artsto.centralbank.di.components.DaggerAppComponent

class App:Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        //appDatabase = AppDatabase.getDatabase(this)
        appComponent = DaggerAppComponent.builder().application(this).build()
    }
}