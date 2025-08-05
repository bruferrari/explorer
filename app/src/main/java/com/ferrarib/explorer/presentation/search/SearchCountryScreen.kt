package com.ferrarib.explorer.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ferrarib.explorer.R
import com.ferrarib.explorer.presentation.theme.ExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCountryScreen(
    onBackButtonClick: () -> Unit,
    onValueChange: (String) -> Unit,
    countries: List<String>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.search_country_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_content_description)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            var search by remember { mutableStateOf("") }

            TextField(
                value = search,
                onValueChange = { searchTerm -> search = searchTerm },
                label = { Text(stringResource(id = R.string.search_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(countries) { country ->
                    Text(
                        text = country,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
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
            countries = listOf("Brazil", "Canada", "Germany", "Japan", "United States")
        )
    }
}
