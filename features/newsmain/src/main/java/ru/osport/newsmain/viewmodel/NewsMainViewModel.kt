package ru.osport.newsmain.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.osport.newsmain.model.Article

internal class NewsMainViewModel : ViewModel() {
    private val _state = MutableStateFlow(State.Empty)
    val state: StateFlow<State>
        get() = _state.asStateFlow()
}

sealed class State {
    class Loading : State()
    class Error : State()
    class Success(val article: List<Article>) : State()
    data object Empty : State()
}