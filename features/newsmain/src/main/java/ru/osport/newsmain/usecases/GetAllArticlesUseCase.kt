package ru.osport.newsmain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.osport.news.data.ArticlesRepository
import ru.osport.news.data.util.RequestResult
import ru.osport.news.data.util.map
import ru.osport.newsmain.model.Article
import ru.osport.news.data.model.Article as DataArticle

class GetAllArticlesUseCase @Inject constructor(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<Article>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticle() }
                }
            }
    }
}

private fun DataArticle.toUiArticle(): Article {
    TODO("Not yet implemented")
}