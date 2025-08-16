package com.ferrarib.explorer.presentation.countrydetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.ui.AppBar
import com.ferrarib.explorer.presentation.theme.ExplorerTheme

@Composable
fun CountryDetailsScreen(
    modifier: Modifier = Modifier,
    countryName: String,
    onBackButtonClick: () -> Unit
) {
    val viewModel: CountryDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(countryName) {
        viewModel.executeAction(Action.FindCountryFullText(countryName))
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.country_details_screen_title),
                onBackButtonClick = onBackButtonClick
            )
        }
    ) { contentPadding ->
        CountryDetailsContent(
            modifier = modifier.padding(contentPadding),
            state = state
        )
    }
}

@Composable
private fun CountryDetailsContent(
    modifier: Modifier = Modifier,
    state: State
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        when (state) {
            is State.Loading -> CircularProgressIndicator()

            is State.Error -> {
                Text("Error: ${state.message}")
            }

            is State.Success -> {
                state.countries.forEach { country ->
                    CountryInfoItem(
                        modifier = Modifier.fillMaxWidth(),
                        country = country
                    )
                }
            }

            is State.Idle -> {
                Text("Loading...")
            }
        }
    }
}

@Composable
private fun CountryInfoItem(
    modifier: Modifier = Modifier,
    country: CountryDto
) {
    Column(modifier = modifier) {
        Text("Name: ${country.name.official}")
        Text("Capital: ${country.capital?.joinToString(", ").orNotApplicable()}")
        Text("Region: ${country.region.orNotApplicable()}")
        Text("Subregion: ${country.subregion.orNotApplicable()}")
        Spacer(Modifier.height(8.dp))
    }
}

private fun String?.orNotApplicable() = this ?: "N/A"

@Preview(showBackground = true)
@Composable
fun CountryInfoItemPreview() {
    ExplorerTheme {
        CountryInfoItem(
            country = CountryDto(
                name = CountryName(
                    common = "Brazil",
                    official = "Federative Republic of Brazil"
                ),
                capital = listOf("Brasília"),
                region = "Americas",
                subregion = "South America",
                flag = "🇧🇷"
            )
        )
    }
}