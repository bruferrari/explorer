package com.ferrarib.explorer.presentation.countrydetails

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.data.models.Maps
import com.ferrarib.explorer.core.ui.AppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme

@Composable
fun CountryDetailsScreen(
    modifier: Modifier = Modifier,
    countryName: String,
    onBackButtonClick: () -> Unit
) {
    val viewModel: CountryDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(countryName) {
        viewModel.executeAction(Action.FindCountryFullText(countryName))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateToGoogleMaps -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.uri)
                    context.startActivity(intent)
                }

                is Effect.NavigateToOpenStreetMaps -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.uri)
                    context.startActivity(intent)
                }
            }
        }
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
            state = state,
            executeAction = viewModel::executeAction
        )
    }
}

@Composable
private fun CountryDetailsContent(
    modifier: Modifier = Modifier,
    state: State,
    executeAction: (Action) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        when (state) {
            is State.Loading, State.Idle -> CircularProgressIndicator()

            is State.Error -> {
                Text(stringResource(R.string.error_prefix, state.message))
            }

            is State.Success -> {
                CountryInfoItem(
                    modifier = Modifier.fillMaxWidth(),
                    country = state.countries.first(),
                    executeAction = executeAction
                )
            }
        }
    }
}


@Composable
private fun CountryInfoItem(
    modifier: Modifier = Modifier,
    country: CountryDto,
    executeAction: (Action) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            stringResource(
                R.string.country_name_label,
                country.name.official
            )
        )
        Text(
            stringResource(
                R.string.country_capital_label,
                country.capital?.joinToString(", ").orNotApplicable()
            )
        )
        Text(
            stringResource(
                R.string.country_region_label,
                country.region.orNotApplicable()
            )
        )
        Text(
            stringResource(
                R.string.country_subregion_label,
                country.subregion.orNotApplicable()
            )
        )

        country.maps?.let { maps ->
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    executeAction(Action.OpenGoogleMaps(maps.googleMaps.toUri()))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.open_google_maps_button))
            }

            Button(
                onClick = {
                    executeAction(Action.OpenOpenStreetMaps(maps.openStreetMaps.toUri()))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.open_openstreetmap_button))
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun String?.orNotApplicable() = this ?: stringResource(R.string.not_applicable)

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
                flag = "🇧🇷",
                maps = Maps(
                    googleMaps = "https://goo.gl/maps/pzEanpDMBs4WLDAAaA",
                    openStreetMaps = "https://www.openstreetmap.org/relation/59470"
                )
            ),
            executeAction = {}
        )
    }
}