package com.ferrarib.explorer.presentation.search

import androidx.lifecycle.viewModelScope
import com.ferrarib.explorer.core.ModelViewIntent
import com.ferrarib.explorer.core.Result
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
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            appLogger.d("countryFound: ${result.data}")
                            _state += State.Success(result.data)
                        }
                        is Result.Error -> {
                            appLogger.e("findCountry: ${result.exception}")
                            _state += State.Error(result.exception.message.orEmpty())
                        }
                    }
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
    }
}
