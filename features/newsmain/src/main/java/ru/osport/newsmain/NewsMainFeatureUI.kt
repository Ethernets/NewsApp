package ru.osport.newsmain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
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
    val currentState = state
    NewsMainContent(currentState, modifier = modifier)
}

@Composable
private fun NewsMainContent(
    currentState: State,
    modifier: Modifier = Modifier,
){
    Column(modifier = modifier) {
        when(currentState){
            is State.Success -> Articles(currentState.articles, modifier = modifier)
            is State.Error -> ArticlesWithError(currentState.articles, modifier = modifier)
            is State.Loading -> ArticlesDuringUpdate(currentState.articles, modifier = modifier)
            State.Empty -> NewsEmpty()
        }
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
    Row {
        article.urlToImage?.let { imageUrl ->
            var isImageVisibility by remember { mutableStateOf(true) }
            if (isImageVisibility) {
                AsyncImage(
                    model = imageUrl,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) isImageVisibility = false
                    },
                    contentDescription = "Article image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier.size(150.dp)
                )
            }
        }
        Spacer(modifier = modifier.size(4.dp))
    }
    Column {
        Text(
            text = article.title ?: "NO TITLE",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        if (article.description != null) {
            Spacer(modifier = modifier.size(4.dp))
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
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
