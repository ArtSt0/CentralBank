package ru.artsto.centralbank.di.components

import android.app.Application
import ru.artsto.centralbank.MainActivity
import ru.artsto.centralbank.di.modules.AppModule
import ru.artsto.centralbank.di.modules.RetrofitModule
import ru.artsto.centralbank.di.modules.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelFactoryModule::class,
    AppModule::class,
    RetrofitModule::class
])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}