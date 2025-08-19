package com.ferrarib.explorer.data.mappers

import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.domain.models.Coordinates

fun CountryDto.toDomain(): Country = Country(
    name = name.common,
    officialName = name.official,
    capital = capital ?: emptyList(),
    region = region,
    subregion = subregion,
    flag = flag,
    googleMapsUrl = maps?.googleMaps,
    openStreetMapsUrl = maps?.openStreetMaps,
    coordinates = if (latLng.size >= 2) {
        Coordinates(latitude = latLng[0], longitude = latLng[1])
    } else null,
    population = population,
    flagUrl = flags.png,
)

fun List<CountryDto>.toDomain(): List<Country> = map { it.toDomain() }