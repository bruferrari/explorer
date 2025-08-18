package com.ferrarib.explorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.core.utils.AppLoggerEntryPoint
import com.ferrarib.explorer.presentation.countrydetails.CountryDetailsScreen
import com.ferrarib.explorer.presentation.home.HomeScreen
import com.ferrarib.explorer.presentation.search.SearchCountryScreen
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object SearchCountryRoute

@Serializable
data class CountryDetailsRoute(val countryName: String)

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
                    logger.i("Country clicked: ${it.officialName}")
                    navController.navigate(CountryDetailsRoute(it.officialName))
                }
            )
        }

        composable<CountryDetailsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<CountryDetailsRoute>()

            CountryDetailsScreen(
                countryName = args.countryName,
                onBackButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
