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
          /*  cache is RequestResult.Success && server is RequestResult.Success -> {

            }*/
            cache is RequestResult.Error && server is RequestResult.Error -> {
                return RequestResult.Error()
            }
            cache is RequestResult.inProgress && server is RequestResult.inProgress -> {
                merge(cache, server)
            }
            cache is RequestResult.Success && server is RequestResult.Error -> {
                merge(cache, server)
            }
        /*    cache is RequestResult.Error && server is RequestResult.Success -> {

            }*/
            cache is RequestResult.Success && server is RequestResult.inProgress -> {
                merge(cache, server)
            }
            cache is RequestResult.inProgress && server is RequestResult.Success -> {
                merge(cache, server)
            }
     /*       cache is RequestResult.Error && server is RequestResult.inProgress -> {

            }
            cache is RequestResult.inProgress && server is RequestResult.Error -> {

            }*/
            else -> {
                return RequestResult.Error()
            }
        }
    }

    private fun merge(cache: RequestResult.inProgress<T>, server: RequestResult.inProgress<T>): RequestResult<T> {
        return when {
            server.data != null -> return RequestResult.inProgress(server.data)
            else -> return RequestResult.inProgress(cache.data)
        }
    }

    private fun merge(cache: RequestResult.Success<T>, server: RequestResult.inProgress<T>): RequestResult<T> {
        return RequestResult.inProgress(cache.data)
    }

    private fun merge(cache: RequestResult.inProgress<T>, server: RequestResult.Success<T>): RequestResult<T> {
        return RequestResult.inProgress(server.data)
    }
    private fun merge(cache: RequestResult.Success<T>, server: RequestResult.Error<T>): RequestResult<T> {
        return RequestResult.Error(data = cache.data, error = server.error)
    }

}