package io.spacenoodles.rxbingo.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule(internal val context: Context) {
    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }
}
