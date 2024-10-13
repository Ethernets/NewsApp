package ru.osport.news.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import ru.osport.news.data.model.Article
import ru.osport.news.data.strategy.MargeStrategy
import ru.osport.news.data.strategy.RequestResponseMargeStrategy
import ru.osport.news.data.util.RequestResult
import ru.osport.news.data.util.map
import ru.osport.news.data.util.toRequestResult
import ru.osport.newsapi.NewsApi
import ru.osport.newsapi.models.ArticleDTO
import ru.osport.newsapi.models.ResponseDTO
import ru.osport.newsdatabase.NewsDataBase
import ru.osport.newsdatabase.models.ArticleDBO


class ArticlesRepository(
    private val database: NewsDataBase,
    private val api: NewsApi,
) {
    fun getAll(
        requestResponseStrategy: MargeStrategy<RequestResult<List<Article>>> = RequestResponseMargeStrategy(),
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()
            .map { result ->
                result.map { articlesDbos ->
                    articlesDbos.map { it.toArticle() }
                }
            }
        val remoteArticles: Flow<RequestResult<List<Article>>>  = getAllFromServer()
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }

        return cachedAllArticles.combine(remoteArticles) { dbos, dtos ->
            requestResponseStrategy.merge(dtos, dtos)
        }.flatMapLatest { result ->
            if (result is RequestResult.Success) {
                database.articlesDao.observeAll()
                    .map { dbos -> dbos.map { it.toArticle() } }
                    .map { RequestResult.Success(it) }
            } else {
                flowOf(result)
            }
        }
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.getEverything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(result.getOrThrow().articles)
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.inProgress())

        return merge(start, apiRequest)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao::getAll.asFlow()
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.inProgress())
        return merge(start, dbRequest)
    }

    suspend fun searchNews(query: String): Flow<Article> {
        TODO("Not yet implemented")
    }
}

