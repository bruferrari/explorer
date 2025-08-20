package com.ferrarib.explorer.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.ui.theme.ExplorerPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackButtonClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = { 
            Text(
                text = title,
                color = Color.White
            ) 
        },
        navigationIcon = {
            onBackButtonClick?.run {
                IconButton(onClick = this) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_content_description),
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExplorerPurple
        )
    )
}
