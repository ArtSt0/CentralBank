package ru.artsto.centralbank.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.artsto.centralbank.di.factory.ViewModelFactory
import ru.artsto.centralbank.di.qualifier.ViewModelKey
import ru.artsto.centralbank.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}
