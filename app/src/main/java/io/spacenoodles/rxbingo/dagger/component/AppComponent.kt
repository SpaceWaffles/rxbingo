package io.spacenoodles.rxbingo.dagger.component

import dagger.Component
import dagger.android.AndroidInjectionModule
import io.spacenoodles.rxbingo.dagger.module.AppModule
import io.spacenoodles.rxbingo.dagger.module.ModelModule
import io.spacenoodles.rxbingo.view.MainActivity
import io.spacenoodles.rxbingo.viewmodel.MainActivityViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        ModelModule::class)
) interface AppComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(mainActivity: MainActivity)
}