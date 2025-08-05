package com.ferrarib.explorer.data.repository

import android.util.Log
import com.ferrarib.explorer.core.data.ExplorerApi
import com.ferrarib.explorer.core.data.models.CountryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExplorerRepository @Inject constructor(
    private val explorerApi: ExplorerApi
) {
    fun findCountry(name: String): Flow<List<CountryDto>> = flow {
        try {
            val result = explorerApi.findCountry(name = name)
            emit(result)
        } catch (e: Exception) {
            Log.e("ExplorerRepository", "Error fetching country", e)
        }
    }
}
