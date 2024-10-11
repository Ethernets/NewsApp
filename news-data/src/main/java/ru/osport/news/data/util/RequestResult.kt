package ru.osport.news.data.util

sealed class RequestResult<out E>(internal val data: E? = null) {
    class inProgress<E>(data: E? = null) : RequestResult<E>(data)
    class Success<E: Any>(data: E) : RequestResult<E>(data)
    class Error<E>(data: E? = null) : RequestResult<E>()
}

internal fun <T : Any> RequestResult<T?>.requireData(): T = checkNotNull(data)
internal fun <I, O > RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> {
            val outData: O = mapper(checkNotNull(data))
            RequestResult.Success(checkNotNull(outData))
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