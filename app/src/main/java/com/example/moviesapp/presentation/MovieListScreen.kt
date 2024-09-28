package com.example.moviesapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.utils.Constants.IMAGE_BASE_URL
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieListScreen(
    onItemClick: (Int) -> Unit,
) {
    val viewModel: MovieViewModel = hiltViewModel()
    val movieResponse by viewModel.moviesListResponse.collectAsState()

    when (movieResponse) {
        is UiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp
                )
            }
        }

        is UiState.Success -> {
            val movie =
                (movieResponse as UiState.Success<Flow<PagingData<Movie>>>).data.collectAsLazyPagingItems()
            if (movie.itemCount == 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 4.dp
                    )
                }
            }
            val listState = rememberLazyGridState()
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 96.dp),
            ) {
                items(movie.itemCount) { index ->
                    val item = movie[index]
                    item?.let {
                        MovieCard(
                            it,
                            onItemClick
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            val message = (movieResponse as UiState.Error).message ?: ""
            Text(
                text = message,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onItemClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { onItemClick(movie.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("$IMAGE_BASE_URL${movie.poster_path}")
                .crossfade(800)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}