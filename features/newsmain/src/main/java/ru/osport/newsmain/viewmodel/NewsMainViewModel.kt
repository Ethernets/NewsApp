package ru.osport.newsmain.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import ru.osport.news.data.ArticlesRepository
import ru.osport.news.data.util.RequestResult
import ru.osport.newsmain.model.Article

internal class NewsMainViewModel(
    private val repository: ArticlesRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = repository.getAll()
        .map { requestResult -> requestResult.toState() }
    val state: StateFlow<State>
        get() = _state.asStateFlow()
}

private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.inProgress -> State.Loading(data ?: emptyList())
        is RequestResult.Success -> State.Success(checkNotNull(data))
        is RequestResult.Error -> State.Error()
    }
}

sealed class State {
    class Loading(val article: List<Article>?) : State()
    class Error : State()
    class Success(val article: List<Article>) : State()
    data object Empty : State()
}