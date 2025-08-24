package com.ferrarib.explorer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ferrarib.explorer.R
import com.ferrarib.explorer.core.ui.theme.FlagPlaceholderBackground
import com.ferrarib.explorer.domain.models.Country

@Composable
fun FlagImage(
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