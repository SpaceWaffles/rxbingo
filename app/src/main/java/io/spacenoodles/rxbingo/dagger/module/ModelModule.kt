package io.spacenoodles.rxbingo.dagger.module

import dagger.Module
import dagger.Provides
import io.spacenoodles.rxbingo.model.BingoGame
import javax.inject.Singleton

@Module
class ModelModule {
    @Provides
    @Singleton
    internal fun provideBingoGame(): BingoGame {
        return BingoGame()
    }
}