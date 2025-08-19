package com.ferrarib.explorer.domain.models

import kotlinx.serialization.Serializable

@Serializable
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
    val population: Long?,
    val flagUrl: String?
)

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)