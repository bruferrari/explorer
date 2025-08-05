package com.ferrarib.explorer.core.di

import com.ferrarib.explorer.core.data.ExplorerApi
import com.ferrarib.explorer.core.data.HttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideExplorerApi(): ExplorerApi {
        return HttpClient.apiService<ExplorerApi>()
    }
}
