package ru.osport.news.data

import android.util.Log
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
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

class ArticlesRepository @Inject constructor(
    private val database: NewsDataBase,
    private val api: NewsApi,
) {
    fun getAll(
        query: String,
        mergeStrategy: MargeStrategy<RequestResult<List<Article>>> = RequestResponseMargeStrategy()
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = gelAllFromDatabase()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer(query)

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest =
            flow { emit(api.getEverything(query = query)) }
                .onEach { result ->
                    if (result.isSuccess) saveArticlesToCache(result.getOrThrow().articles)
                }
                .onEach { result ->
                    if (result.isFailure) {
                        Log.e(
                            LOG_TAG,
                            "Error getting data from server. Cause = ${result.exceptionOrNull()}"
                        )
                    }
                }
                .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.inProgress())
        return merge(apiRequest, start)
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.map { response -> response.articles.map { it.toArticle() } }
            }
    }

    private suspend fun saveArticlesToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun gelAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest =
            database.articlesDao::getAll.asFlow()
                .map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> { RequestResult.Success(it) }
                .catch {
                    Log.e(LOG_TAG, "Error getting from database. Cause = $it")
                    emit(RequestResult.Error(error = it))
                }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.inProgress())

        return merge(start, dbRequest).map { result ->
            result.map { dbos -> dbos.map { it.toArticle() } }
        }
    }

    private companion object {
        const val LOG_TAG = "ArticlesRepository"
    }
}