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
    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = "android")
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.Empty)
}



private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.inProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
        is RequestResult.Error -> State.Error(data)
    }
}

public sealed class State(public open val articles: List<Article>?) {

    public data object Empty : State(articles = null)

    public class Loading(articles: List<Article>? = null) : State(articles)

    public class Error(articles: List<Article>? = null) : State(articles)

    public class Success(override val articles: List<Article>) : State(articles)
}