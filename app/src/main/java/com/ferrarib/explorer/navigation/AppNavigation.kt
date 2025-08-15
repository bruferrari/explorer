package com.ferrarib.explorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.core.utils.AppLoggerEntryPoint
import com.ferrarib.explorer.presentation.home.HomeScreen
import com.ferrarib.explorer.presentation.search.SearchCountryScreen
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object SearchCountryRoute

@Composable
fun AppNavigation() {
    val appLoggerEntryPoint =
        EntryPointAccessors.fromApplication(
            LocalContext.current.applicationContext,
            AppLoggerEntryPoint::class.java
        )
    val logger: AppLogger = appLoggerEntryPoint.appLogger()

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(onNavigateToSearch = { navController.navigate(SearchCountryRoute) })
        }
        composable<SearchCountryRoute> {
            val viewModel: SearchCountryViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            SearchCountryScreen(
                onValueChange = { searchTerm ->
                    viewModel.executeAction(
                        SearchCountryViewModel.Action.FindCountry(searchTerm)
                    )
                },
                onBackButtonClick = {
                    navController.popBackStack()
                },
                state = state,
                onCountryClick = {
                    logger.i("AppNavigation", "Country clicked: ${it.name.official}")
                }
            )
        }
    }
}
