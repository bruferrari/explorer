package com.ferrarib.explorer.presentation.search

import androidx.lifecycle.viewModelScope
import com.ferrarib.explorer.core.ModelViewIntent
import com.ferrarib.explorer.core.data.models.CountryDto
import com.ferrarib.explorer.core.di.AppDispatchers
import com.ferrarib.explorer.core.di.Dispatcher
import com.ferrarib.explorer.core.utils.AppLogger
import com.ferrarib.explorer.data.repository.ExplorerRepository
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel.Action
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel.Effect
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCountryViewModel @Inject constructor(
    private val repository: ExplorerRepository,
    private val appLogger: AppLogger,
    @Dispatcher(AppDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
) : ModelViewIntent<Action, Effect, State>() {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.FindCountry -> findCountry(action.name)
            is Action.FindCountryFullText -> findCountryFullText(action.name)
        }
    }

    override fun initialValue(): State = State.Idle

    private fun findCountry(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            repository
                .findCountry(name)
                .onStart {
                    appLogger.d("findCountry: onStart")
                    _state += State.Loading
                }
                .catch { exception ->
                    appLogger.e("findCountry: $exception")
                    _state += State.Error(exception.message.orEmpty())
                }
                .collect { countries ->
                    appLogger.d("countryFound: $countries")
                    _state += State.Success(countries)
                }
        }
    }

    private fun findCountryFullText(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            repository
                .findCountryFullText(name)
                .onStart {
                    appLogger.d("findCountryFullText: onStart")
                    _state += State.Loading
                }
                .catch { exception ->
                    appLogger.e("findCountryFullText: $exception")
                    _state += State.Error(exception.message.orEmpty())
                }
                .collect { countries ->
                    appLogger.d("countryFoundFullText: $countries")
                    _state += State.Success(countries)
                }
        }
    }

    sealed interface State {
        data object Idle : State
        data object Loading : State
        data class Error(val message: String) : State
        data class Success(val countries: List<CountryDto>) : State
    }

    sealed interface Effect

    sealed interface Action {
        data class FindCountry(val name: String) : Action
        data class FindCountryFullText(val name: String) : Action
    }
}
