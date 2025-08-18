package com.ferrarib.explorer.core.data

import com.ferrarib.explorer.core.data.models.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExplorerApi {

    @GET("name/{name}")
    suspend fun findCountry(@Path("name") name: String): List<CountryDto>

}
