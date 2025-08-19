package com.ferrarib.explorer.data.repository

import app.cash.turbine.test
import com.ferrarib.explorer.core.Result
import com.ferrarib.explorer.core.data.ExplorerApi
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.domain.models.Country
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExplorerRepositoryTest {

    @Mock
    private lateinit var explorerApi: ExplorerApi

    @Mock
    private lateinit var logger: AppLogger

    private lateinit var repository: ExplorerRepository

    private val mockCountryDto = CountryDto(
        name = CountryName(common = "Brazil", official = "Federative Republic of Brazil"),
        capital = listOf("Brasília"),
        region = "Americas",
        subregion = "South America",
        flag = "🇧🇷",
        maps = null,
        latLng = listOf(-14.235004, -51.92528),
        population = 215313498,
        flags = com.ferrarib.explorer.core.data.models.Flags(
            png = "https://flagcdn.com/w320/br.png",
            svg = "https://flagcdn.com/w320/br.svg"
        )
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ExplorerRepository(explorerApi, logger)
    }

    @Test
    fun `findCountry success should emit Success result with mapped countries`() = runTest {
        val countryName = "Brazil"
        val apiResponse = listOf(mockCountryDto)
        whenever(explorerApi.findCountry(countryName)).thenReturn(apiResponse)

        repository.findCountry(countryName).test {
            val result = awaitItem()
            
            assertTrue(result is Result.Success)
            assertEquals(1, result.data.size)
            
            val country = result.data.first()
            assertEquals("Brazil", country.name)
            assertEquals("Federative Republic of Brazil", country.officialName)
            assertEquals(listOf("Brasília"), country.capital)
            assertEquals("Americas", country.region)
            assertEquals("South America", country.subregion)
            assertEquals("🇧🇷", country.flag)
            assertEquals(215313498L, country.population)
            assertEquals(-14.235004, country.coordinates?.latitude)
            assertEquals(-51.92528, country.coordinates?.longitude)
            assertEquals("https://flagcdn.com/w320/br.png", country.flagUrl)
            
            awaitComplete()
        }
    }

    @Test
    fun `findCountry success with empty list should emit Success result with empty list`() = runTest {
        val countryName = "NonExistentCountry"
        whenever(explorerApi.findCountry(countryName)).thenReturn(emptyList())

        repository.findCountry(countryName).test {
            val result = awaitItem()
            
            assertTrue(result is Result.Success)
            assertTrue(result.data.isEmpty())
            
            awaitComplete()
        }
    }

    @Test
    fun `findCountry error should emit Error result and log exception`() = runTest {
        val countryName = "ErrorCountry"
        val exception = RuntimeException("Network error")
        whenever(explorerApi.findCountry(countryName)).thenThrow(exception)

        repository.findCountry(countryName).test {
            val result = awaitItem()
            
            assertTrue(result is Result.Error)
            assertEquals(exception, result.exception)
            
            awaitComplete()
        }
    }
}