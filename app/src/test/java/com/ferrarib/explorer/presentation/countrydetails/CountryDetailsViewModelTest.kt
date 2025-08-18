package com.ferrarib.explorer.presentation.countrydetails

import android.net.Uri
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

class CountryDetailsViewModelTest {

    private lateinit var viewModel: CountryDetailsViewModel

    @Before
    fun setUp() {
        viewModel = CountryDetailsViewModel()
    }

    @Test
    fun `executeAction with OpenGoogleMaps should emit NavigateToGoogleMaps effect`() = runTest {
        val mockUri: Uri = mock()

        viewModel.effect.test {
            viewModel.executeAction(Action.OpenGoogleMaps(mockUri))
            
            val effect = awaitItem()
            assertEquals(Effect.NavigateToGoogleMaps(mockUri), effect)
        }
    }

    @Test
    fun `executeAction with OpenOpenStreetMaps should emit NavigateToOpenStreetMaps effect`() = runTest {
        val mockUri: Uri = mock()

        viewModel.effect.test {
            viewModel.executeAction(Action.OpenOpenStreetMaps(mockUri))
            
            val effect = awaitItem()
            assertEquals(Effect.NavigateToOpenStreetMaps(mockUri), effect)
        }
    }
}
