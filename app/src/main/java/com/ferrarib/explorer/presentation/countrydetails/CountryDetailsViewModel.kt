package com.ferrarib.explorer.presentation.countrydetails

import android.net.Uri
import com.ferrarib.explorer.core.ModelViewIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(): ModelViewIntent<Action, Effect, Unit>() {
    
    override fun executeAction(action: Action) {
        _effect += when (action) {
            is Action.OpenGoogleMaps -> Effect.NavigateToGoogleMaps(action.uri)
            is Action.OpenOpenStreetMaps -> Effect.NavigateToOpenStreetMaps(action.uri)
        }
    }

    override fun initialValue(): Unit = Unit
}

sealed interface Action {
    data class OpenGoogleMaps(val uri: Uri) : Action
    data class OpenOpenStreetMaps(val uri: Uri) : Action
}

sealed interface Effect {
    data class NavigateToGoogleMaps(val uri: Uri) : Effect
    data class NavigateToOpenStreetMaps(val uri: Uri) : Effect
}
