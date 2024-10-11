package ru.osport.news.data.strategy

import ru.osport.news.data.util.RequestResult

interface RequestResponseStrategy<E> {
    fun merge(right: RequestResult<E>, left: RequestResult<E>): RequestResult<E>
}