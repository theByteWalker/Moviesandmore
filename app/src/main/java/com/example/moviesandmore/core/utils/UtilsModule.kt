package com.example.moviesandmore.core.utils

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    @Singleton
    abstract fun bindLogger(logcatLogger: LogcatLogger): Logger
}
