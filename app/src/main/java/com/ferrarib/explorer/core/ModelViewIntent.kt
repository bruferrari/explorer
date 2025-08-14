package com.ferrarib.explorer.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class ModelViewIntent<Action, Effect, State>: ViewModel() {

    protected val _state: MutableStateFlow<State> = MutableStateFlow(value = initialValue())
    val state: StateFlow<State> get() = _state

    protected val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect> get() = _effect

    abstract fun executeAction(action: Action)

    abstract fun initialValue(): State

    protected operator fun <S> MutableStateFlow<S>.plusAssign(newState: S) {
        this.value = newState
    }

    protected operator fun <E> MutableSharedFlow<E>.plusAssign(newEffect: E) {
        this.tryEmit(newEffect)
    }
}
