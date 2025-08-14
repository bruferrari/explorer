package com.ferrarib.explorer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.ui.MenuButton
import com.ferrarib.explorer.navigation.SearchCountryRoute // Updated import

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MenuButton(
                label = stringResource(id = R.string.find_country_button_label),
                onClick = { navController.navigate(SearchCountryRoute) }
            )
        }
    }
}
