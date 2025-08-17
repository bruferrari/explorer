package com.ferrarib.explorer.core.utils

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppLoggerEntryPoint {
    fun appLogger(): AppLogger
}
