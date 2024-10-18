package ru.osport.newsapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.osport.news_core.AppDispatchers
import ru.osport.newsapi.NewsApi
import ru.osport.newsapp.BuildConfig
import ru.osport.newsdatabase.NewsDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient? {
        if(BuildConfig.DEBUG) {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        return null
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient,
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDataBase = NewsDataBase(context)

    @Provides
    @Singleton
    fun provideAppCoroutinesDispatcher(): AppDispatchers = AppDispatchers()

}