package com.ferrarib.explorer.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("name")
    val name: CountryName,
    @SerialName("capital")
    val capital: List<String>? = emptyList(),
    @SerialName("region")
    val region: String? = null,
    @SerialName("subregion")
    val subregion: String? = null,
    @SerialName("flag")
    val flag: String? = null,
    @SerialName("maps")
    val maps: Maps? = null,
    @SerialName("latlng")
    val latLng: List<Double> = emptyList(),
    @SerialName("population")
    val population: Long? = null,
    @SerialName("flags")
    val flags: Flags,
)

@Serializable
data class CountryName(
    val common: String,
    val official: String,
)

@Serializable
data class Maps(
    @SerialName("googleMaps")
    val googleMaps: String,
    @SerialName("openStreetMaps")
    val openStreetMaps: String,
)

@Serializable
data class Flags(
    val png: String,
    val svg: String,
)
