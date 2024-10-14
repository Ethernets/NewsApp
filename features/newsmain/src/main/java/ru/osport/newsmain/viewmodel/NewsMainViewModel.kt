package ru.osport.newsmain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.osport.news.data.util.RequestResult
import ru.osport.newsmain.model.Article
import ru.osport.newsmain.usecases.GetAllArticlesUseCase
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
) : ViewModel() {
    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.Empty)
}



private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.inProgress -> State.Loading(data ?: emptyList())
        is RequestResult.Success -> State.Success(data)
        is RequestResult.Error -> State.Error()
    }
}

sealed class State {
    class Loading(val article: List<Article>?) : State()
    class Error : State()
    class Success(val article: List<Article>) : State()
    data object Empty : State()
}