package com.ferrarib.explorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.presentation.countrydetails.CountryDetailsScreen
import com.ferrarib.explorer.presentation.home.HomeScreen
import com.ferrarib.explorer.presentation.search.SearchCountryScreen
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object HomeRoute

@Serializable
object SearchCountryRoute

@Serializable
data class CountryDetailsRoute(val country: Country)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchCountryRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(onNavigateToSearch = { navController.navigate(SearchCountryRoute) })
        }

        composable<SearchCountryRoute> {
            val viewModel: SearchCountryViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            SearchCountryScreen(
                onValueChange = { searchTerm ->
                    viewModel.executeAction(
                        SearchCountryViewModel.Action.FindCountry(searchTerm)
                    )
                },
                state = state,
                onCountryClick = {
                    navController.navigate(CountryDetailsRoute(it))
                },
                onClearSearchClick = {
                    viewModel.executeAction(SearchCountryViewModel.Action.ClearSearch)
                }
            )
        }

        composable<CountryDetailsRoute>(
            typeMap = mapOf(typeOf<Country>() to CountryNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<CountryDetailsRoute>()

            CountryDetailsScreen(
                country = args.country,
                onBackButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
