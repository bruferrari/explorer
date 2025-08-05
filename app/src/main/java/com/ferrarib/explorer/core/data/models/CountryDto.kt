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
)

@Serializable
data class CountryName(
    val common: String,
    val official: String,
)
