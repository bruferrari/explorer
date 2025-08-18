package com.ferrarib.explorer.data.repository

import com.ferrarib.explorer.core.Result
import com.ferrarib.explorer.core.data.ExplorerApi
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.data.mappers.toDomain
import com.ferrarib.explorer.domain.models.Country
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
    fun findCountry(name: String): Flow<Result<List<Country>>> = flow {
        try {
            val result = explorerApi.findCountry(name = name)
            emit(Result.success(result.toDomain()))
        } catch (e: Exception) {
            logger.e("Error fetching country", e)
            emit(Result.error(e))
        }
    }

}
