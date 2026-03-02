# Movies and More - Coding Standards & Guidelines

This document outlines the coding standards and best practices followed in the Movies and More Android application. All developers should refer to this file when writing code or implementing features.

## Table of Contents

1. [Architecture](#architecture)
2. [Kotlin & Language Standards](#kotlin--language-standards)
3. [Project Structure](#project-structure)
4. [Naming Conventions](#naming-conventions)
5. [Code Organization](#code-organization)
6. [Dependency Injection](#dependency-injection)
7. [Composables & UI Development](#composables--ui-development)
8. [ViewModels & State Management](#viewmodels--state-management)
9. [Data Layer](#data-layer)
10. [Domain Layer](#domain-layer)
11. [Error Handling & Logging](#error-handling--logging)
12. [Best Practices](#best-practices)

---

## Architecture

### Clean Architecture Pattern

The project follows **Clean Architecture** principles with three distinct layers:

1. **Presentation Layer** (`presentation/`)
   - UI components (Composables)
   - ViewModels
   - Navigation logic
   - UI state management

2. **Domain Layer** (`domain/`)
   - Use Cases (business logic)
   - Repository interfaces
   - Data models
   - No Android dependencies

3. **Data Layer** (`data/`)
   - Repository implementations
   - API services
   - Database entities and DAOs
   - Networking configuration

### Design Patterns Used

- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** for data abstraction
- **Use Case Pattern** for business logic encapsulation
- **Intent/State Pattern** for UI event handling
- **Dependency Injection** via Hilt

---

## Kotlin & Language Standards

### Language Version
- **Language**: Kotlin
- **JVM Target**: Java 11
- **Min SDK**: API 24
- **Target SDK**: API 36

### Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Aim for 120 characters max
- **Naming**: camelCase for variables/functions, PascalCase for classes
- **Immutability**: Prefer `val` over `var`
- **Null Safety**: Use null-coalescing (`?.`) and safe casts (`as?`)

### Example:
```kotlin
// Good
val userName: String = "John"
val age: Int = 25

// Avoid
var userName: String = "John"
val user_name: String = "John"
```

---

## Project Structure

```
app/src/main/java/com/example/moviesandmore/
├── presentation/
│   ├── MainActivity.kt
│   ├── Routes.kt
│   ├── Destination.kt
│   ├── ui/
│   │   ├── components/
│   │   │   ├── PopularMovies.kt
│   │   │   ├── FavouriteMovies.kt
│   │   │   ├── MovieDetails.kt
│   │   │   ├── MovieCard.kt
│   │   │   └── ...
│   │   └── theme/
│   ├── *ViewModel.kt
│   └── *Contract.kt
├── domain/
│   ├── MovieRepository.kt (interface)
│   ├── GetAllTitlesUseCase.kt
│   ├── GetMovieDetailsUseCase.kt
│   ├── ToggleFavouriteUseCase.kt
│   └── ...
├── data/
│   ├── MovieRepositoryImpl.kt
│   ├── MovieApiService.kt
│   ├── MoviePagingSource.kt
│   ├── FavouritesDao.kt
│   ├── FavouriteMovieEntity.kt
│   └── ...
└── app/
    ├── DatabaseModule.kt
    └── ...
```

---

## Naming Conventions

### Classes & Interfaces

- **Classes**: PascalCase
- **Data Classes**: Suffix with meaningful names
- **Interfaces**: PascalCase (no "I" prefix)
- **Use Cases**: `Get[Entity]UseCase`, `Toggle[Action]UseCase`
- **DAOs**: `[Entity]Dao`
- **Entities**: `[Entity]Entity`

### Variables & Functions

- **Variables**: camelCase, descriptive names
- **Private Variables**: Use `_` prefix for backing properties (e.g., `_uiState`)
- **State**: Use `uiState` or `[Feature]State` suffix
- **Functions**: camelCase, verb-based names
- **Intent/Contract Classes**: Use `[Feature]Intent` and `[Feature]State` pattern

### Examples:
```kotlin
// Classes
class MovieDetailsViewModel
interface MovieRepository
data class MovieDetailResponse
data class MovieDetailState
sealed class MovieDetailIntent

// Variables
val movieTitle: String
private val _favoriteMovies = mutableStateOf<List<Movie>>(emptyList())
val favoriteMovies: State<List<Movie>> = _favoriteMovies

// Functions
fun handleIntent(intent: MovieDetailIntent)
fun toggleFavorite(movie: MovieDetailResponse)
fun fetchMovieDetails(titleId: String)
```

---

## Code Organization

### File Organization

1. **Package declaration** at the top
2. **Imports** (organized by androidx, com.example, etc.)
3. **Annotations** (e.g., @Composable, @HiltViewModel)
4. **Class/Function declaration**
5. **Companion objects** at the end

### Composable Organization

```kotlin
@Composable
fun MovieDetails(titleId: String, viewModel: MovieDetailsViewModel = hiltViewModel()) {
    // 1. State and state hoisting
    val state = viewModel.uiState
    
    // 2. Side effects (LaunchedEffect, etc.)
    LaunchedEffect(titleId) {
        viewModel.handleIntent(MovieDetailIntent.LoadMovie(titleId))
    }
    
    // 3. Conditional rendering
    if (state.isLoading) {
        // Loading UI
    } else {
        // Main UI
    }
}
```

---

## Dependency Injection

### Hilt Configuration

- Use `@HiltViewModel` for ViewModels
- Use `@Inject` for constructor injection
- Use `@AndroidEntryPoint` for Activities
- Define modules in `app/` package (e.g., `DatabaseModule.kt`, `NetworkModule.kt`)

### Example:
```kotlin
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    // ...
}
```

---

## Composables & UI Development

### Jetpack Compose Standards

- **Preview**: Use `@Preview` annotations for all Composables
- **Modifiers**: Always pass as first parameter after required parameters
- **Theming**: Use `MoviesAndMoreTheme` and `MaterialTheme` colors
- **State Hoisting**: Lift state to parent Composables when needed
- **Lambda Parameters**: Place at the end of function signature

### Composable Template:
```kotlin
@Composable
fun MovieCard(movie: MovieTitle, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        // Card content
    }
}
```

### Material 3 Components

- Use `MaterialTheme.colorScheme` for colors
- Use `MaterialTheme.typography` for text styles
- Follow Material 3 design guidelines
- Use `CenterAlignedTopAppBar` for consistent styling

---

## ViewModels & State Management

### ViewModel Pattern

```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    // dependencies
) : ViewModel() {
    // Private backing property
    private val _uiState = mutableStateOf<FeatureState>(FeatureState())
    
    // Public read-only state
    val uiState: State<FeatureState> = _uiState
    
    // Intent handling
    fun handleIntent(intent: FeatureIntent) {
        when (intent) {
            // Handle intents
        }
    }
    
    // Scope work in viewModelScope
    private fun someAsyncWork() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Work
            } catch (e: Exception) {
                Log.e("App", "Error: ${e.message}")
            }
        }
    }
}
```

### State Management

- Use `mutableStateOf` for compose state
- Use `State<T>` for immutable exposure
- Use `by` delegate for property delegation
- Separate UI state from loading state

---

## Data Layer

### Repository Implementation

```kotlin
@Inject
class MovieRepositoryImpl(
    private val apiService: MovieApiService,
    private val favMoviesDao: FavoriteDao
) : MovieRepository {
    
    override suspend fun getMovieDetails(titleId: String): MovieDetailResponse {
        return apiService.getMovieDetails(titleId)
    }
    
    override suspend fun addFavorite(movie: MovieDetailResponse) {
        val entity = FavoriteMovieEntity(
            id = movie.id,
            title = movie.primaryTitle,
            // ... map other properties
        )
        favMoviesDao.insertFavorite(entity)
    }
}
```

### Room DAO Standards

```kotlin
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)
    
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>
    
    @Query("DELETE FROM favorites WHERE id = :titleId")
    suspend fun deleteFavoriteById(titleId: String)
}
```

### Database Entities

```kotlin
@Entity(tableName = "favorites")
data class FavoriteMovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String?,
    val rating: Int?,
    val year: Int?,
    val createdAt: Long = System.currentTimeMillis()
)
```

---

## Domain Layer

### Use Case Pattern

```kotlin
class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<MovieTitle>> {
        return repository.getMoviesPaging()
    }
}
```

### Repository Interface

```kotlin
interface MovieRepository {
    fun getMoviesPaging(): Flow<PagingData<MovieTitle>>
    suspend fun getMovieDetails(titleId: String): MovieDetailResponse
    suspend fun isMovieFavorite(titleId: String): Boolean
    suspend fun addFavorite(movie: MovieDetailResponse)
    suspend fun removeFavorite(titleId: String)
}
```

---

## Error Handling & Logging

### Logging Standards

- Use `Log.d()` for debug information
- Use `Log.e()` for errors
- Include meaningful error messages
- Log tag: Use class name or feature name

### Example:
```kotlin
try {
    val response = getMovieDetailsUseCase(titleId)
} catch (e: Exception) {
    Log.e("MoviesAndMore", "Error fetching movie: ${e.message}")
}
```

### Exception Handling

- Always wrap network/database calls in try-catch
- Propagate exceptions to UI via state
- Provide user-friendly error messages in UI

---

## Best Practices

### General

1. **Keep functions small** and focused (Single Responsibility Principle)
2. **Prefer composition over inheritance**
3. **Use sealed classes** for restricted hierarchies
4. **Avoid Magic Numbers** - use named constants
5. **Document complex logic** with comments
6. **Use meaningful variable names** - avoid abbreviations
7. **Test edge cases** - use JUnit and Mockito

### Coroutines

- Always launch in `viewModelScope` for ViewModels
- Use `Dispatchers.IO` for network/database operations
- Use `Dispatchers.Main` for UI updates (default)
- Properly handle cancellation

### Paging

- Use `androidx.paging.Pager` for list pagination
- Implement `PagingSource` for custom pagination logic
- Use `collectAsLazyPagingItems()` in Compose

### Navigation

- Use sealed classes for routes/destinations
- Define navigation in `Routes.kt` and `Destination.kt`
- Handle back navigation properly

### Resource Management

- Use `AsyncImage` from Coil for image loading
- Always provide `contentDescription` for accessibility
- Use proper `ContentScale` values

---

## Git & Code Review Standards

### Commit Messages

- Use present tense: "Add feature" not "Added feature"
- Be descriptive: "Add favorites tab implementation" not "Fix"
- Reference issues when applicable

### Code Review Checklist

- [ ] Code follows naming conventions
- [ ] Architecture patterns are followed
- [ ] Error handling is present
- [ ] No hardcoded strings (use resources)
- [ ] Tests are written for logic
- [ ] Code is properly commented for complex logic

---

## Dependencies Used

- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM, Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit, OkHttp
- **Database**: Room
- **Async**: Coroutines, Flow
- **Images**: Coil 3
- **Paging**: AndroidX Paging 3
- **Testing**: JUnit, Mockito

---

## Version Info

- **Kotlin**: Latest stable
- **Java**: 11
- **Android Gradle Plugin**: Latest
- **Compose**: Material 3

---

**Last Updated**: March 2026

For questions or updates to these standards, please consult with the team lead.

