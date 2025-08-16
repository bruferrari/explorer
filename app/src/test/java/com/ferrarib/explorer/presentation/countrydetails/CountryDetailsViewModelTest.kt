package com.ferrarib.explorer.presentation.countrydetails

import app.cash.turbine.test
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.data.repository.ExplorerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CountryDetailsViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var viewModel: CountryDetailsViewModel

    @Mock
    private lateinit var repository: ExplorerRepository

    @Mock
    private lateinit var logger: AppLogger

    private val mockCountry = CountryDto(
        name = CountryName(common = "Mock Country", official = "Mock Country")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = CountryDetailsViewModel(repository, logger, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `findCountryFullText success from repository should emit Loading and then Success`() =
        runTest(testDispatcher) {
            val countryName = "United"
            val countries = listOf(
                mockCountry.copy(
                    name = CountryName(
                        common = "United States",
                        official = "United States of America"
                    )
                )
            )
            whenever(repository.findCountryFullText(countryName))
                .thenReturn(flowOf(countries))

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(
                    Action.FindCountryFullText(
                        countryName
                    )
                )

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Success(countries), awaitItem())
            }
        }

    @Test
    fun `findCountryFullText failure from repository should emit Loading and then Error`() =
        runTest(testDispatcher) {
            val countryName = "InvalidSearchTerm"
            val expectedException = RuntimeException("API error")
            whenever(repository.findCountryFullText(countryName))
                .thenReturn(flow { throw expectedException })

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(
                    Action.FindCountryFullText(
                        countryName
                    )
                )

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Error(expectedException.message.orEmpty()), awaitItem())
            }
        }

    @Test
    fun `executeAction with FindCountryFullText should trigger findCountryFullText`() =
        runTest(testDispatcher) {
            val countryName = "Kingdom"
            val countries = listOf(
                mockCountry.copy(
                    name = CountryName(
                        common = "United Kingdom",
                        official = "United Kingdom of Great Britain and Northern Ireland"
                    )
                )
            )
            whenever(repository.findCountryFullText(countryName))
                .thenReturn(flowOf(countries))

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(
                    Action.FindCountryFullText(
                        countryName
                    )
                )

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Success(countries), awaitItem())
            }
        }

    @Test
    fun `findCountryFullText with null exception message should use empty string`() =
        runTest(testDispatcher) {
            val countryName = "TestCountry"
            val exceptionWithNullMessage = object : RuntimeException() {
                override val message: String? = null
            }
            whenever(repository.findCountryFullText(countryName))
                .thenReturn(flow { throw exceptionWithNullMessage })

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(
                    Action.FindCountryFullText(
                        countryName
                    )
                )

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Error(""), awaitItem())
            }
        }
}