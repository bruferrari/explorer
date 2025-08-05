package com.ferrarib.explorer.core.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object HttpClient {

    val json = Json { ignoreUnknownKeys = true }

    fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    inline fun <reified T> apiService(): T {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient())
            .addConverterFactory(
                json.asConverterFactory(contentType)
            )
            .build()
            .create(T::class.java)
    }

    const val BASE_URL = "https://restcountries.com/v3.1/"
}
