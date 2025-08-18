package com.ferrarib.explorer.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.ferrarib.explorer.domain.models.Country
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CountryNavType : NavType<Country>(isNullableAllowed = false) {
    
    override fun put(bundle: Bundle, key: String, value: Country) {
        bundle.putString(key, serializeAsValue(value))
    }
    
    override fun get(bundle: Bundle, key: String): Country? {
        return bundle.getString(key)?.let { parseValue(it) }
    }
    
    override fun serializeAsValue(value: Country): String {
        val json = Json.encodeToString(value)
        return Uri.encode(json)
    }
    
    override fun parseValue(value: String): Country {
        val decodedJson = Uri.decode(value)
        return Json.decodeFromString<Country>(decodedJson)
    }
}