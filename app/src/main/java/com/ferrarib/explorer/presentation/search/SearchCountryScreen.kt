package com.ferrarib.explorer.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.data.models.CountryName
import com.ferrarib.explorer.core.ui.AppBar
import com.ferrarib.explorer.core.ui.theme.ExplorerTheme
import kotlinx.coroutines.delay

private const val DEBOUNCE_TIME_IN_MILLIS = 800L

@Composable
fun SearchCountryScreen(
    onBackButtonClick: () -> Unit,
    onValueChange: (String) -> Unit,
    state: SearchCountryViewModel.State,
    onCountryClick: (CountryDto) -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.search_country_screen_title),
                onBackButtonClick = onBackButtonClick
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
            TextField(
                value = search,
                onValueChange = { searchTerm -> search = searchTerm },
                label = { Text(stringResource(id = R.string.search_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when (state) {
                is SearchCountryViewModel.State.Success -> {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                        items(state.countries) { countryDto ->
                            val flag = countryDto.flag
                            val officialName = countryDto.name.official
                            val displayText = if (!flag.isNullOrBlank()) {
                                "$flag $officialName"
                            } else {
                                officialName
                            }
                            Text(
                                text = displayText,
                                modifier = Modifier
                                    .clickable { onCountryClick(countryDto) }
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

                SearchCountryViewModel.State.Idle -> Unit
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchCountryScreenPreview() {
    ExplorerTheme {
        SearchCountryScreen(
            onBackButtonClick = {},
            onValueChange = {},
            state = SearchCountryViewModel.State.Success(
                countries = listOf(
                    CountryDto(
                        name = CountryName(
                            common = "Brazil",
                            official = "Federative Republic of Brazil"
                        ),
                        capital = listOf("Brasília"),
                        region = "Americas",
                        subregion = "South America",
                        flag = "🇧🇷"
                    ),
                    CountryDto(
                        name = CountryName(
                            common = "Germany",
                            official = "Federal Republic of Germany"
                        ),
                        capital = listOf("Berlin"),
                        region = "Europe",
                        subregion = "Western Europe",
                        flag = "🇩🇪"
                    ),
                    CountryDto(
                        name = CountryName(common = "Canada", official = "Canada"),
                        capital = listOf("Ottawa"),
                        region = "Americas",
                        subregion = "North America",
                        flag = "" // Example of an empty flag
                    ),
                    CountryDto(
                        name = CountryName(common = "Japan", official = "Japan"),
                        capital = listOf("Tokyo"),
                        region = "Asia",
                        subregion = "Eastern Asia"
                        // Flag is null by default
                    )
                )
            ),
            onCountryClick = {}
        )
    }
}
