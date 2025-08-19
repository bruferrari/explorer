package com.ferrarib.explorer.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ferrarib.explorer.R
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.core.ui.ExplorerAppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme
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
            ExplorerAppBar(
                title = stringResource(R.string.app_title)
            )
        }
    ) { innerPadding ->
        var search by remember { mutableStateOf("") }

        LaunchedEffect(search) {
            if (search.isNotEmpty()) {
                delay(DEBOUNCE_TIME_IN_MILLIS)
                onValueChange(search)
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            )

            when (state) {
                is SearchCountryViewModel.State.Success -> {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                        items(state.countries) { country ->
                            val flag = country.flag
                            val officialName = country.officialName
                            val displayText = if (!flag.isNullOrBlank()) {
                                "$flag $officialName"
                            } else {
                                officialName
                            }
                            Text(
                                text = displayText,
                                modifier = Modifier
                                    .clickable { onCountryClick(country) }
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }

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
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
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
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        }
        
        Text(
            text = stringResource(R.string.empty_state_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = stringResource(R.string.empty_state_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
                        population = null
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
                        population = null
                    ),
                    Country(
                        name = "Canada",
                        officialName = "Canada",
                        capital = listOf("Ottawa"),
                        region = "Americas",
                        subregion = "North America",
                        flag = "", // Example of an empty flag
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null
                    ),
                    Country(
                        name = "Japan",
                        officialName = "Japan",
                        capital = listOf("Tokyo"),
                        region = "Asia",
                        subregion = "Eastern Asia",
                        flag = null, // Flag is null by default
                        googleMapsUrl = null,
                        openStreetMapsUrl = null,
                        coordinates = null,
                        population = null
                    )
                )
            ),
            onCountryClick = {},
            onClearSearchClick = {}
        )
    }
}
