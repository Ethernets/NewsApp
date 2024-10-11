package ru.osport.newsmain.usecases

import ru.osport.news.data.ArticlesRepository

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {
    operator fun invoke() = repository.getAll()
}