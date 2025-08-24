package com.ferrarib.explorer.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.ui.ExplorerAppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme
import com.ferrarib.explorer.core.ui.theme.FlagPlaceholderBackground
import com.ferrarib.explorer.core.ui.theme.SearchBackground
import com.ferrarib.explorer.core.ui.theme.SearchFieldBackground
import com.ferrarib.explorer.domain.models.Country
import kotlinx.coroutines.delay

private const val DEBOUNCE_TIME_IN_MILLIS = 800L

@Composable
fun SearchCountryScreen(
    onValueChange: (String) -> Unit,
    state: SearchCountryViewModel.State,
    onCountryClick: (Country) -> Unit,
    onClearSearchClick: () -> Unit
) {
    Scaffold(
        topBar = {
            ExplorerAppBar(title = stringResource(R.string.app_title))
        }
    ) { innerPadding ->
        var search by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()

            if (state is SearchCountryViewModel.State.Success && search.isEmpty()) {
                onClearSearchClick()
            }
        }

        LaunchedEffect(search) {
            if (search.isNotEmpty()) {
                delay(DEBOUNCE_TIME_IN_MILLIS)
                onValueChange(search)
            }
        }

        Column(
            modifier = Modifier
                .background(SearchBackground)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(SearchFieldBackground)
                    .focusRequester(focusRequester),
                value = search,
                onValueChange = { searchTerm -> search = searchTerm },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                trailingIcon = {
                    if (search.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onClearSearchClick()
                                search = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.clear_search_content_description)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )

            when (state) {
                is SearchCountryViewModel.State.Success -> CountriesList(
                    state = state,
                    onCountryClick = onCountryClick,
                )

                is SearchCountryViewModel.State.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.generic_error_message),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                SearchCountryViewModel.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator()
                    }
                }

                SearchCountryViewModel.State.Idle -> EmptyState()
            }
        }
    }
}

@Composable
private fun CountriesList(
    state: SearchCountryViewModel.State.Success,
    modifier: Modifier = Modifier,
    onCountryClick: (Country) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.countries) { country ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCountryClick(country) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        FlagImage(
                            modifier = Modifier.size(width = 64.dp, height = 40.dp),
                            country = country
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = country.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            if (country.capital.isNotEmpty()) {
                                Text(
                                    text = country.capital.first(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FlagImage(
    country: Country,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = country.flagUrl,
        contentDescription = "Flag of ${country.name}",
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(FlagPlaceholderBackground),
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_globe)
    )
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_globe),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.padding(top = 24.dp))

        Text(
            text = stringResource(R.string.empty_state_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.empty_state_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchCountryScreenEmptyStatePreview() {
    ExplorerTheme {
        SearchCountryScreen(
            onValueChange = {},
            state = SearchCountryViewModel.State.Idle,
            onCountryClick = {},
            onClearSearchClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchCountryScreenPreview() {
    ExplorerTheme {
        SearchCountryScreen(
            onValueChange = {},
            state = SearchCountryViewModel.State.Success(
                countries = listOf(
                    Country(
                        name = "Brazil",
                        officialName = "Federative Republic of Brazil",
                        capital = listOf("Brasília"),
                        region = "Americas",
                        subregion = "South America",
                        flag = "🇧🇷",
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null,
                        flagUrl = "https://flagcdn.com/w320/br.png"
                    ),
                    Country(
                        name = "Germany",
                        officialName = "Federal Republic of Germany",
                        capital = listOf("Berlin"),
                        region = "Europe",
                        subregion = "Western Europe",
                        flag = "🇩🇪",
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null,
                        flagUrl = "https://flagcdn.com/w320/de.png"
                    ),
                    Country(
                        name = "Canada",
                        officialName = "Canada",
                        capital = listOf("Ottawa"),
                        region = "Americas",
                        subregion = "North America",
                        flag = "",
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null,
                        flagUrl = "https://flagcdn.com/w320/ca.png"
                    ),
                    Country(
                        name = "Japan",
                        officialName = "Japan",
                        capital = listOf("Tokyo"),
                        region = "Asia",
                        subregion = "Eastern Asia",
                        flag = null,
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null,
                        flagUrl = "https://flagcdn.com/w320/jp.png"
                    )
                )
            ),
            onCountryClick = {},
            onClearSearchClick = {}
        )
    }
}
