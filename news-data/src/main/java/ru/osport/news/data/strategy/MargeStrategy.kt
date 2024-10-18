package ru.osport.news.data.strategy

import ru.osport.news.data.util.RequestResult

interface MargeStrategy<E> {
    fun merge(cache: E, server: E): E
}

internal class RequestResponseMargeStrategy<T: Any>: MargeStrategy<RequestResult<T>>{
    override fun merge(
        cache: RequestResult<T>,
        server: RequestResult<T>
    ): RequestResult<T> {
        return when{
            cache is RequestResult.inProgress && server is RequestResult.inProgress -> merge(cache, server)
            cache is RequestResult.Success && server is RequestResult.inProgress -> merge(cache, server)
            cache is RequestResult.inProgress && server is RequestResult.Success -> merge(cache, server)
            cache is RequestResult.Success && server is RequestResult.Success -> merge(cache, server)
            cache is RequestResult.Success && server is RequestResult.Error -> merge(cache, server)
            cache is RequestResult.inProgress && server is RequestResult.Error -> merge(cache, server)
            cache is RequestResult.Error && server is RequestResult.inProgress -> merge(cache, server)
            cache is RequestResult.Error && server is RequestResult.Success -> merge(cache, server)
            else -> error("Unimplemented branch right=$cache & left=$server")
        }
    }

    private fun merge(
        cache: RequestResult.inProgress<T>,
        server: RequestResult.inProgress<T>
    ): RequestResult<T> {
        return when {
            server.data != null -> RequestResult.inProgress(server.data)
            else -> RequestResult.inProgress(cache.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.inProgress<T>
    ): RequestResult<T> {
        return RequestResult.inProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: RequestResult.inProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.inProgress(server.data)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(data = server.data)
    }

    private fun merge(
        cache: RequestResult.inProgress<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = server.data ?: cache.data, error = server.error)
    }

    private fun merge(
        cache: RequestResult.Error<T>,
        server: RequestResult.inProgress<T>
    ): RequestResult<T> {
        return server
    }

    private fun merge(
        cache: RequestResult.Error<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return server
    }
}