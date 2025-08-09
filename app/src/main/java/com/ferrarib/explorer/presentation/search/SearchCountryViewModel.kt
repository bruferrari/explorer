package com.ferrarib.explorer.presentation.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ferrarib.explorer.core.ModelViewIntent
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCountryViewModel @Inject constructor(
    private val repository: ExplorerRepository,
    private val appLogger: AppLogger,
    @Dispatcher(AppDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
) : ModelViewIntent<Action, Effect, State>() {

    init {
        // temp
        executeAction(Action.FindCountry("Brazil"))
    }

    override fun executeAction(action: Action) {
        when (action) {
            is Action.FindCountry -> findCountry(action.name)
        }
    }

    override fun initialValue(): State = State.Loading

    private fun findCountry(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            repository.findCountry(name)
                .catch { exception ->
                    appLogger.e("SearchCountryViewModel", "findCountry: $exception")
                }
                .collect { countries ->
                    appLogger.d("SearchCountryViewModel", "countryFound: ${countries.first()}")
                }
        }
    }

    sealed interface State {
        data object Loading : State
    }

    sealed interface Effect

    sealed interface Action {
        data class FindCountry(val name: String) : Action
    }
}
