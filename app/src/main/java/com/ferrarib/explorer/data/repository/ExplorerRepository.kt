package com.ferrarib.explorer.data.repository

import com.ferrarib.explorer.core.data.ExplorerApi
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressWarnings("TooGenericExceptionCaught")
class ExplorerRepository @Inject constructor(
    private val explorerApi: ExplorerApi,
    private val logger: AppLogger,
) {
    fun findCountry(name: String): Flow<List<CountryDto>> = flow {
        try {
            val result = explorerApi.findCountry(name = name)
            emit(result)
        } catch (e: Exception) {
            logger.e("Error fetching country", e)
            throw e
        }
    }

    fun findCountryFullText(name: String): Flow<List<CountryDto>> = flow {
        try {
            val result = explorerApi.findCountryFullText(name = name)
            emit(result)
        } catch (e: Exception) {
            logger.e("Error fetching country with full text", e)
            throw e
        }
    }
}
