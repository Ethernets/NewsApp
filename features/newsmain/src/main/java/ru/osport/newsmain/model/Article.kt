package ru.osport.newsmain.model

import ru.osport.newsapi.models.SourceDTO

data class Article(
    val source: SourceDTO,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
)