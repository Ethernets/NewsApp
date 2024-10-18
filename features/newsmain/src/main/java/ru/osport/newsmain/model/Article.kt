package ru.osport.newsmain.model

data class Article(
    val id: Long,
    val title: String?,
    val author: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val content: String?,
    val publishedAt: String?,
)