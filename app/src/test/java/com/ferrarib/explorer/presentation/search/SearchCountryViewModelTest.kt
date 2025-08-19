package com.ferrarib.explorer.presentation.search

import app.cash.turbine.test
import com.ferrarib.explorer.core.Result
import com.ferrarib.explorer.domain.models.Country
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

    private val mockCountry = Country(
        name = "Mock Country",
        officialName = "Mock Country",
        capital = emptyList(),
        region = null,
        subregion = null,
        flag = null,
        googleMapsUrl = null,
        openStreetMapsUrl = null,
        coordinates = null,
        population = null,
        flagUrl = "https://flagcdn.com/w320/br.png"
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
            whenever(repository.findCountry(countryName)).thenReturn(flowOf(Result.success(countries)))

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
                .thenReturn(flowOf(Result.error(expectedException)))

            viewModel.state.test {
                assertEquals(State.Idle, awaitItem())

                viewModel.executeAction(SearchCountryViewModel.Action.FindCountry(countryName))

                assertEquals(State.Loading, awaitItem())
                assertEquals(State.Error(expectedException.message.orEmpty()), awaitItem())
            }
        }
}
