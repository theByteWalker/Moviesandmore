# Movies and More

Movies and More is a modern Android application built to browse movie information. It leverages the latest Android development tools and libraries, including Jetpack Compose for UI, Hilt for Dependency Injection, and Coroutines for asynchronous operations, all structured around Clean Architecture principles.

## Features

-   **Browse Popular Movies**: Fetches and displays a list of popular movie titles from a remote API.
-   **Navigation**: Structured bottom navigation bar allowing users to switch between "Popular", "Favourites", and "Search" screens.
-   **Clean Architecture**: Separation of concerns into Presentation, Domain, and Data layers.
-   **Modern UI**: Fully built with Jetpack Compose using Material 3 design components.

## Tech Stack

-   **Language**: [Kotlin](https://kotlinlang.org/)
-   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
-   **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
-   **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
-   **Concurrency**: [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) & [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
-   **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture
-   **JSON Parsing**: [Gson](https://github.com/google/gson) (via Retrofit converter)
-   **Testing**: JUnit, Mockito

## Architecture Overview

The project follows Clean Architecture principles, dividing the codebase into three main layers:

1.  **Presentation Layer** (`com.example.moviesandmore.presentation`):
    -   Contains UI components (Composables) and ViewModels.
    -   `MainActivity`: Sets up the navigation graph and application scaffolding.
    -   `MyViewModel`: Manages UI state and interacts with use cases.

2.  **Domain Layer** (`com.example.moviesandmore.domain`):
    -   Contains business logic and interfaces.
    -   `GetAllTitlesUseCase`: Encapsulates the logic for retrieving movie titles.
    -   `MovieRepository`: Defines the contract for data operations.

3.  **Data Layer** (`com.example.moviesandmore.data`):
    -   Handles data retrieval and storage.
    -   `MovieRepositoryImpl`: Implements the domain repository interface.
    -   `MovieApiService`: Defines the Retrofit API endpoints.
    -   `NetworkModule`: Configures networking components (OkHttp, Retrofit).

## Setup & Installation

### Prerequisites
-   Android Studio Koala or newer recommended.
-   JDK 11 or higher.
-   Android SDK API Level 36 (target), API Level 24 (min).

### Steps
1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd Moviesandmore
    ```

2.  **Open in Android Studio**:
    -   Launch Android Studio.
    -   Select "Open" and navigate to the cloned directory.

3.  **Sync Project**:
    -   Allow Gradle to sync and download dependencies.

4.  **Run the App**:
    -   Connect an Android device or start an emulator.
    -   Click the **Run** button (green arrow) or press `Shift + F10`.

## API Configuration

The application is configured to communicate with `https://api.imdbapi.dev/`. Networking configuration can be found in `NetworkModule.kt`.
