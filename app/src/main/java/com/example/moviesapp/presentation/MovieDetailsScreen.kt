package com.example.moviesapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.R
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.utils.Constants.IMAGE_BASE_URL

@Composable
fun MovieDetailsScreen(movieId: Int) {
    val viewModel: MovieViewModel = hiltViewModel()
    val movieDetailsResponse by viewModel.movieDetailsResponse.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.onIntent(MovieIntent.GetMovieDetails(movieId))
    }

    when (movieDetailsResponse) {
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

        is UiState.Error -> {
            val message = (movieDetailsResponse as UiState.Error).message ?: ""
            Text(
                text = message,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }

        is UiState.Success -> {
            val details = (movieDetailsResponse as UiState.Success<MovieDetails>).data
            MovieDetail(details)
        }
    }
}

@Composable
fun MovieDetail(details: MovieDetails) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("$IMAGE_BASE_URL${details.backdrop_path}")
                .crossfade(500)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = details.title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("$IMAGE_BASE_URL${details.poster_path}")
                    .crossfade(500)
                    .build(),
                contentDescription = null,
                modifier = Modifier.height(150.dp),
                contentScale = ContentScale.Inside,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                InfoItem(stringResource(R.string.language), details.original_language)
                InfoItem(stringResource(R.string.release_date), details.release_date)
                InfoItem(stringResource(R.string.popularity), details.popularity.toString())
                InfoItem(
                    stringResource(R.string.runtime),
                    details.runtime.toString() + stringResource(R.string.mins)
                )
                InfoItem(stringResource(R.string.status), details.status)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(24.dp),
            text = details.overview,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun InfoItem(name: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "$name:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}