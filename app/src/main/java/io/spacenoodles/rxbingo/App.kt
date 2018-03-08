package io.spacenoodles.rxbingo

import android.app.Application
import io.spacenoodles.rxbingo.dagger.component.AppComponent
import io.spacenoodles.rxbingo.dagger.component.DaggerAppComponent
import io.spacenoodles.rxbingo.dagger.module.AppModule

class App : Application() {
    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}