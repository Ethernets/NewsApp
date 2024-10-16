package ru.osport.newsmain

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import ru.osport.newsmain.model.Article
import ru.osport.newsmain.viewmodel.State

@Composable
internal fun ArticleList(
    articleState: State.Success,
    modifier: Modifier = Modifier
) {
    ArticleList(articles = articleState.articles, modifier)
}


@Composable
internal fun ArticleList(
    articles: List<Article>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(articles) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}