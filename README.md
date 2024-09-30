# Movies App

A simple and modern Android application that allows users to browse and discover movies. Built with
Jetpack Compose for the UI and backed by a RESTful API for fetching movie data.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Key Technologies](#key-technologies)
- [API](#api)
- [Setup](#setup)
  - [Pre-requisites](#pre-requisites)
  - [Installation](#installation)
- [Screenshots](#screenshots)
- 
### Features

- **Browse Popular Movies**: See the latest trending and popular movies.
- **Movie Details**: View detailed information about each movie including the language, release date, runtime, overview and more.
- **Offline Support**: Cached movies for offline viewing.

### Table of Contents 

### Architecture

The app is built using the **MVI (Model-View-Intent)** pattern combined with Clean Architecture principles. The key components are:
- **ViewModel**: Manages UI-related state and handles business logic. It processes user intents, interacts with use cases, and emits view states for the UI.
- **Use Cases**: Encapsulate specific business logic into single-purpose classes, ensuring separation of concerns.
- **Repository**: The single source of truth, managing data from remote and local sources, handling retrieval, caching, and synchronization for online and offline access.
- **Data Sources**: Fetches data from the network (REST API) and local storage (Room database for caching and offline access).

### Key Technologies

- **Kotlin**: 100% Kotlin for all code.
- **Jetpack Compose**: Modern Android UI toolkit for building native interfaces declaratively.
- **Compose navigation**: For handling screen transitions and back stack management in Jetpack Compose apps.
- **Hilt**: Dependency Injection framework for managing app-wide dependencies.
- **Retrofit**: For making HTTP requests to fetch movie data from the API.
- **Coroutines**: For managing background tasks and async code.
- **StateFlow**: To observe data and update the UI reactively.
- **Room**: Local database for storing movies for offline viewing.

### API

- This product uses the TMDB API but is not endorsed or certified by TMDB.

### Setup:

#### Pre-requisites

- **Android Studio:** Make sure to use the latest stable version.
- **Java Version:** JDK 8 or higher is required.
- **Android SDK:** Comes with Android Studio, but ensure it's up to date.
- **Android Device or Emulator** - Use a physical device with USB debugging enabled or create an Android Virtual Device (AVD) in Android Studio.

#### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/jamesli1/MoviesApp.git
   ```

2. Open the project in Android Studio
3. Build and run the project on your Android device or emulator


## Screenshots
<img width="250" alt="1" src="https://github.com/user-attachments/assets/bab9e202-9775-4ccf-8679-92cefa1492f6"> &nbsp;&nbsp;&nbsp;
<img width="250" alt="3" src="https://github.com/user-attachments/assets/20eedc3c-2dc2-4765-a01b-9770cbf2c401"> <br>
<img width="250" alt="2" src="https://github.com/user-attachments/assets/045396f6-21ad-4576-98d6-44d2c5df491e"> &nbsp;&nbsp;&nbsp;
<img width="250" alt="4" src="https://github.com/user-attachments/assets/130fa7fb-9bd3-4a0f-a23b-0bf48ff7f1f4"> <br>

