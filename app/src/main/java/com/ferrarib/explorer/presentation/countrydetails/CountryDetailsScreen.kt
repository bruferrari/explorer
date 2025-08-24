package com.ferrarib.explorer.presentation.countrydetails

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.ui.CountryCard
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.core.ui.ExplorerAppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme
import com.ferrarib.explorer.core.ui.theme.MapPlaceholderBackground

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
            ExplorerAppBar(
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
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MapPlaceholder(country = country)

        CountryCard(
            modifier = Modifier.padding(16.dp),
            country = country,
            showTrailingIcon = false,
            clickable = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        CountryInfoSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            country = country
        )

        Spacer(modifier = Modifier.height(24.dp))

        ActionButtons(
            modifier = Modifier.padding(horizontal = 16.dp),
            country = country,
            executeAction = executeAction
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MapPlaceholder(
    country: Country,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MapPlaceholderBackground),
        contentAlignment = Alignment.TopStart
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View larger map",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun CountryInfoSection(
    modifier: Modifier = Modifier,
    country: Country
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InfoRow(
            label = "Capital",
            value = country.capital.joinToString(", ").ifBlank { "N/A" }
        )

        Separator()
        
        InfoRow(
            label = "Region",
            value = buildString {
                country.region?.let { append(it) }
                if (country.region != null && country.subregion != null) {
                    append(" ")
                }
                country.subregion?.let { append(it) }
            }.ifBlank { "N/A" }
        )

        Separator()
        
        InfoRow(
            label = "Population",
            value = country.population?.let { "%,d".format(it) } ?: "N/A"
        )
    }
}

@Composable
private fun Separator(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(1.dp)
            .background(Color.LightGray)
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActionButtons(
    modifier: Modifier = Modifier,
    country: Country,
    executeAction: (Action) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        country.googleMapsUrl?.let { googleMapsUrl ->
            Button(
                onClick = {
                    executeAction(Action.OpenGoogleMaps(googleMapsUrl.toUri()))
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Google Maps")
            }
        }

        country.openStreetMapsUrl?.let { openStreetMapsUrl ->
            Button(
                onClick = {
                    executeAction(Action.OpenOpenStreetMaps(openStreetMapsUrl.toUri()))
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Street View")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryDetailsScreenPreview() {
    ExplorerTheme {
        CountryDetailsScreen(
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
                coordinates = null,
                flagUrl = "https://flagcdn.com/w320/br.png"
            ),
            onBackButtonClick = {}
        )
    }
}