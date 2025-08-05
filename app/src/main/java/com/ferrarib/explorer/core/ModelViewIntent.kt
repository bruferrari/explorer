package com.ferrarib.explorer.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ModelViewIntent<Action, Effect, State>: ViewModel() {

    protected val _state: MutableStateFlow<State> = MutableStateFlow(value = initialValue())
    protected val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()

    abstract fun executeAction(action: Action): Unit

    abstract fun initialValue(): State

    protected operator fun <S> MutableStateFlow<S>.plusAssign(newState: S) {
        this.value = newState
    }

    protected operator fun <E> MutableSharedFlow<E>.plusAssign(newEffect: E) {
        this.tryEmit(newEffect)
    }
}
