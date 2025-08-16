package com.ferrarib.explorer.presentation.search

import app.cash.turbine.test
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.data.repository.ExplorerRepository
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel.State
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
class SearchCountryViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var viewModel: SearchCountryViewModel

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
        viewModel = SearchCountryViewModel(repository, logger, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fndCountry success from repository should emit Loading and then Success`() =
        runTest(testDispatcher) {
            val countryName = "Germany"
            val countries = listOf(mockCountry)
            whenever(repository.findCountry(countryName)).thenReturn(flowOf(countries))

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(SearchCountryViewModel.Action.FindCountry(countryName))

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Success(countries), awaitItem())
            }
        }

    @Test
    fun `findCountry failure from repository should emit Loading and then Error`() =
        runTest(testDispatcher) {
            val countryName = "NonExistentCountry"
            val expectedException = RuntimeException("Network error")
            whenever(repository.findCountry(countryName))
                .thenReturn(flow { throw expectedException })

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(SearchCountryViewModel.Action.FindCountry(countryName))

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Error(expectedException.message.orEmpty()), awaitItem())
            }
        }

}