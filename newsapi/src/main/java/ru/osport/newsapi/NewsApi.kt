package ru.osport.newsapi

import androidx.annotation.IntRange
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import ru.osport.newsapi.models.ArticleDTO
import ru.osport.newsapi.models.ResponseDTO
import ru.osport.newsapi.utils.ApiKeyInterceptor
import ru.osport.newsapi.utils.Languages
import ru.osport.newsapi.utils.SortBy
import java.util.Date

/*
* API details [here] https://newsapi.org/docs/endpoints/everything
*/

interface NewsApi {
    @GET("/everything")
    suspend fun getEverything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<Languages>? = null,
        @Query("sortBy") sortBy: SortBy? = SortBy.POPULARITY,
        @Query("pageSize") @IntRange(from = 1, to = 20) pageSize: Int? = 20,
        @Query("page") @IntRange(from = 1) page: Int? = 1,
    ): Result<ResponseDTO<ArticleDTO>>
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
): NewsApi {
    val retrofit = retrofit(baseUrl, apiKey, okHttpClient)
    return retrofit.create()
}

private fun retrofit(baseUrl: String, apiKey: String, okHttpClient: OkHttpClient?): Retrofit {
    val modifiedOkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(ApiKeyInterceptor(apiKey = apiKey))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}