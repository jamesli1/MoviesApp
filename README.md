# Movies App

A simple and modern Android application that allows users to browse and discover movies. Built with
Jetpack Compose for the UI and backed by a RESTful API for fetching movie data.

## Features

- Browse Popular Movies: See the latest trending and popular movies.
- Movie Details: View detailed information about each movie including the language, release date,
  runtime, overview and more.
- Offline Support: Cached movies for offline viewing.

## Architecture

- The app follows MVI + Clean Architecture with the following components:
- ViewModel: Handles UI-related data and business logic.
- Use Cases: separate single purpose Use Case
- Repository: Manages data operations and acts as a mediator between the ViewModel and data sources.
- Data Sources: Retrieves data from the network (REST API) and local database (using Room database
  library).

## Tech Stack

- Kotlin: 100% Kotlin for all code.
- Jetpack Compose: Modern Android UI toolkit for building native interfaces declaratively.
- Hilt: Dependency Injection framework for managing app-wide dependencies.
- Retrofit: For making HTTP requests to fetch movie data from the API.
- Coroutines: For managing background tasks and async code.
- StateFlow: To observe data and update the UI reactively.
- Room: Local database for storing movies for offline viewing.

## API

The app uses The Movie Database (TMDb) for fetching movie data.
This product uses the TMDB API but is not endorsed or certified by TMDB.

## Prerequisites

- Android Studio
- JDK 8 or higher
- Android SDK
- Android Device or Emulator

## Installation

To run the app locally:

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the project on your Android device or emulator.

## Screenshots
<img width="210" alt="1" src="https://github.com/user-attachments/assets/bab9e202-9775-4ccf-8679-92cefa1492f6">
<img width="210" alt="2" src="https://github.com/user-attachments/assets/045396f6-21ad-4576-98d6-44d2c5df491e">
<img width="210" alt="3" src="https://github.com/user-attachments/assets/20eedc3c-2dc2-4765-a01b-9770cbf2c401">
<img width="210" alt="4" src="https://github.com/user-attachments/assets/130fa7fb-9bd3-4a0f-a23b-0bf48ff7f1f4">

