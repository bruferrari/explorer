package com.ferrarib.explorer.presentation.countrydetails

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.ferrarib.explorer.R
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.core.ui.AppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme

@Composable
fun CountryDetailsScreen(
    modifier: Modifier = Modifier,
    country: Country,
    onBackButtonClick: () -> Unit
) {
    val viewModel: CountryDetailsViewModel = hiltViewModel()

    val context = LocalContext.current

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
            country = country,
            executeAction = viewModel::executeAction
        )
    }
}

@Composable
private fun CountryDetailsContent(
    modifier: Modifier = Modifier,
    country: Country,
    executeAction: (Action) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        CountryInfoItem(
            modifier = Modifier.fillMaxWidth(),
            country = country,
            executeAction = executeAction
        )
    }
}


@Composable
private fun CountryInfoItem(
    modifier: Modifier = Modifier,
    country: Country,
    executeAction: (Action) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            stringResource(
                R.string.country_name_label,
                country.officialName
            )
        )
        Text(
            stringResource(
                R.string.country_capital_label,
                country.capital.joinToString(", ").ifBlank { stringResource(R.string.not_applicable) }
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
        Text(
            stringResource(
                R.string.country_population_label,
                country.population?.let { "%,d".format(it) }.orNotApplicable()
            )
        )
        
        country.coordinates?.let { coordinates ->
            Text(
                stringResource(
                    R.string.country_coordinates_label,
                    stringResource(
                        R.string.coordinates_format,
                        coordinates.latitude.toFloat(),
                        coordinates.longitude.toFloat()
                    )
                )
            )
        }

        if (country.googleMapsUrl != null || country.openStreetMapsUrl != null) {
            Spacer(Modifier.height(16.dp))

            country.googleMapsUrl?.let { googleMapsUrl ->
                Button(
                    onClick = {
                        executeAction(Action.OpenGoogleMaps(googleMapsUrl.toUri()))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.open_google_maps_button))
                }
            }

            country.openStreetMapsUrl?.let { openStreetMapsUrl ->
                Button(
                    onClick = {
                        executeAction(Action.OpenOpenStreetMaps(openStreetMapsUrl.toUri()))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.open_openstreetmap_button))
                }
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
            country = Country(
                name = "Brazil",
                officialName = "Federative Republic of Brazil",
                capital = listOf("Brasília"),
                region = "Americas",
                subregion = "South America",
                flag = "🇧🇷",
                population = 215313498,
                googleMapsUrl = "https://goo.gl/maps/pzEanpDMBs4WLDAAaA",
                openStreetMapsUrl = "https://www.openstreetmap.org/relation/59470",
                coordinates = null
            ),
            executeAction = {}
        )
    }
}
