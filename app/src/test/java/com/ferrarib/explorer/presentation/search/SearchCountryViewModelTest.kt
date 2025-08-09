package com.ferrarib.explorer.presentation.search

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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchCountryViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var viewModel: SearchCountryViewModel

    @Mock
    private lateinit var repository: ExplorerRepository

    @Mock
    private lateinit var logger: AppLogger

    // Adjust this mockCountry to reflect your actual Country data class structure
    private val mockCountry = CountryDto(
        name = CountryName(common = "Mock Country", official = "Mock Country")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `executeAction FindCountry should call findCountry on repository`() = runTest(testDispatcher) {
        val countryName = "Germany"
        whenever(repository.findCountry(countryName)).thenReturn(flowOf(listOf(mockCountry)))

        viewModel = SearchCountryViewModel(repository, logger, testDispatcher)

        viewModel.executeAction(SearchCountryViewModel.Action.FindCountry(countryName))
        advanceUntilIdle()

        verify(repository).findCountry(countryName)
    }

    @Test
    fun `findCountry failure from repository should be caught and logged`() = runTest(testDispatcher) {
        val countryName = "NonExistentCountry"
        val expectedException = RuntimeException("Network error")
        whenever(repository.findCountry(countryName)).thenReturn(flow { throw expectedException })

        viewModel = SearchCountryViewModel(repository, logger, testDispatcher)

        viewModel.executeAction(SearchCountryViewModel.Action.FindCountry(countryName))
        advanceUntilIdle()

        verify(repository).findCountry(countryName)
        // Test passes if no unhandled exception is thrown
    }
}