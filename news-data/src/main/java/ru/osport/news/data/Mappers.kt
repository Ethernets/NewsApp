package ru.osport.news.data

import ru.osport.news.data.model.Article
import ru.osport.news.data.model.Source
import ru.osport.newsapi.models.ArticleDTO
import ru.osport.newsapi.models.SourceDTO
import ru.osport.newsdatabase.models.ArticleDBO
import ru.osport.newsdatabase.models.SourceDBO

internal fun ArticleDBO.toArticle(): Article {
    return Article(
        source = source.toSource(),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage,
    )
}

private fun SourceDBO.toSource(): Source {
    return Source(
        id = id ?: name,
        name = name,
    )

}

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        source = source.toSource(),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage,
    )
}

private fun SourceDTO.toSource(): Source {
    return Source(
        id = id ?: name,
        name = name,
    )

}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO{
    return ArticleDBO(
        source = source.toSourceDbo(),
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        title = title,
        url = url,
        urlToImage = urlToImage,
    )
}

private fun SourceDTO.toSourceDbo(): SourceDBO {
    return SourceDBO(
        id = id ?: name,
        name = name,
    )
}
