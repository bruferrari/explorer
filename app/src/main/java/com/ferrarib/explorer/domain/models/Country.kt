package com.ferrarib.explorer.domain.models

data class Country(
    val name: String,
    val officialName: String,
    val capital: List<String>,
    val region: String?,
    val subregion: String?,
    val flag: String?,
    val googleMapsUrl: String?,
    val openStreetMapsUrl: String?,
    val coordinates: Coordinates?,
    val population: Long?
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)