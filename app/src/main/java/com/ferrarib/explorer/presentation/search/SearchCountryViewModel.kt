package com.ferrarib.explorer.presentation.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ferrarib.explorer.core.ModelViewIntent
import com.ferrarib.explorer.data.repository.ExplorerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.ferrarib.explorer.presentation.search.SearchCountryViewModel.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class SearchCountryViewModel @Inject constructor(
    private val repository: ExplorerRepository
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
        viewModelScope.launch {
            repository.findCountry(name)
                .catch { exception ->
                    Log.e("SearchCountryViewModel", "findCountry: $exception")
                }
                .collect { countries ->
                    Log.d("SearchCountryViewModel", "countryFound: ${countries.first()}")
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
