package com.ferrarib.explorer.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // Keep this
import androidx.navigation.compose.rememberNavController
import com.ferrarib.explorer.presentation.home.HomeScreen
import com.ferrarib.explorer.presentation.search.SearchCountryScreen
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel
import kotlinx.serialization.Serializable // Add this import

@Serializable
object HomeRoute

@Serializable
object SearchCountryRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(navController = navController)
        }
        composable<SearchCountryRoute> {
            val viewModel: SearchCountryViewModel = hiltViewModel()

            SearchCountryScreen(
                onValueChange = {},
                onBackButtonClick = {
                    navController.popBackStack()
                },
                countries = emptyList()
            )
        }
    }
}
