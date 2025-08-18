package com.ferrarib.explorer.presentation.countrydetails

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.ferrarib.explorer.core.ModelViewIntent
import com.ferrarib.explorer.core.Result
import com.ferrarib.explorer.domain.models.Country
import com.ferrarib.explorer.core.di.AppDispatchers
import com.ferrarib.explorer.core.di.Dispatcher
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.data.repository.ExplorerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val repository: ExplorerRepository,
    private val appLogger: AppLogger,
    @Dispatcher(AppDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
): ModelViewIntent<Action, Effect, State>() {
    
    override fun executeAction(action: Action) {
        when (action) {
            is Action.FindCountryFullText -> findCountryFullText(action.name)
            is Action.OpenGoogleMaps -> {
                _effect += Effect.NavigateToGoogleMaps(action.uri)
            }
            is Action.OpenOpenStreetMaps -> {
                _effect += Effect.NavigateToOpenStreetMaps(action.uri)
            }
        }
    }

    override fun initialValue(): State = State.Idle

    private fun findCountryFullText(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            repository
                .findCountryFullText(name)
                .onStart {
                    appLogger.d("findCountryFullText: onStart")
                    _state += State.Loading
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            appLogger.d("countryFoundFullText: ${result.data}")
                            _state += State.Success(result.data)
                        }
                        is Result.Error -> {
                            appLogger.e("findCountryFullText: ${result.exception}")
                            _state += State.Error(result.exception.message.orEmpty())
                        }
                    }
                }
        }
    }
}

sealed interface State {
    data object Idle : State
    data object Loading : State
    data class Error(val message: String) : State
    data class Success(val countries: List<Country>) : State
}

sealed interface Action {
    data class FindCountryFullText(val name: String) : Action
    data class OpenGoogleMaps(val uri: Uri) : Action
    data class OpenOpenStreetMaps(val uri: Uri) : Action
}

sealed interface Effect {
    data class NavigateToGoogleMaps(val uri: Uri) : Effect
    data class NavigateToOpenStreetMaps(val uri: Uri) : Effect
}
