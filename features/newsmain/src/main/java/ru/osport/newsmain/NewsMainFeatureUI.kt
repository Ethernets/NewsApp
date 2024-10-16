package ru.osport.newsmain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.Provides
import ru.osport.newsmain.model.Article
import ru.osport.newsmain.viewmodel.NewsMainViewModel
import ru.osport.newsmain.viewmodel.State

@Composable
fun NewsMain(modifier: Modifier = Modifier) {
    NewsMain(viewModel = viewModel(), modifier = modifier)
}

@Composable
internal fun NewsMain(viewModel : NewsMainViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    when(val currentState = state){
        is State.Success -> Articles(currentState.articles, modifier = modifier)
        is State.Error -> ArticlesWithError(currentState.articles, modifier = modifier)
        is State.Loading -> ArticlesDuringUpdate(currentState.articles, modifier = modifier)
        State.Empty -> NewsEmpty()
    }
}

@Composable
internal fun ArticlesWithError(articles: List<Article>?, modifier: Modifier = Modifier) {
    Column {
        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) { Text("Error during update", color = MaterialTheme.colorScheme.onError) }
        if (articles != null) Articles(articles = articles)
    }
}

@Composable
internal fun ArticlesDuringUpdate(articles: List<Article>?, modifier: Modifier = Modifier) {
    Column {
        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        if (articles != null) Articles(articles = articles)
    }
}

@Composable
internal fun NewsEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "No news")
    }
}

@Composable
private fun Articles(articles: List<Article>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(articles){ article ->
            key(article.id){
                Article(article, modifier = modifier)
            }

        }
    }
}

@Preview
@Composable
internal fun Article(@PreviewParameter(ArticlePreviewProvider::class) article: Article, modifier: Modifier = Modifier) {
    Column {
        Text(text = article.title, style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Spacer(modifier = modifier.size(4.dp))
        Text(text = article.description, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
    }
}

private class ArticlePreviewProvider: PreviewParameterProvider<Article> {
    override val values = sequenceOf(
        Article(
            id = 1,
            title = "Title",
            author = "Author",
            description = "Description",
            url = "Url",
            urlToImage = "UrlToImage",
            content = "Content",
            publishedAt = "PublishedAt"
        ),Article(
            id = 1,
            title = "Title",
            author = "Author",
            description = "Description",
            url = "Url",
            urlToImage = "UrlToImage",
            content = "Content",
            publishedAt = "PublishedAt"
        ),Article(
            id = 1,
            title = "Title",
            author = "Author",
            description = "Description",
            url = "Url",
            urlToImage = "UrlToImage",
            content = "Content",
            publishedAt = "PublishedAt"
        ),
    )
}
