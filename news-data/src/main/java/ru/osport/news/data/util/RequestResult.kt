package ru.osport.news.data.util

sealed class RequestResult<out E: Any>(val data: E? = null) {
    class inProgress<E: Any>(data: E? = null) : RequestResult<E>(data)
    class Success<E: Any>(data: E) : RequestResult<E>(data)
    class Error<E: Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

fun <I: Any, O: Any > RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> {
            val outData: O = mapper(checkNotNull(data))
            RequestResult.Success(outData)
        }
        is RequestResult.inProgress -> RequestResult.inProgress(data?.let(mapper))
        is RequestResult.Error -> RequestResult.Error(data?.let(mapper))
    }
}

internal fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }
}