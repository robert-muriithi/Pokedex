# Pokédex

A modern Android application built with Jetpack Compose that displays a list of Pokemon using the [PokeAPI](https://pokeapi.co/). The app features offline-first architecture with efficient pagination, search functionality, and beautiful UI with shared element transitions.

## Features

- **Browse Pokemon**: Infinite scrolling list of all Pokemon with lazy loading
- **Search**: Real-time search functionality to find Pokemon by name or ID
- **Pokemon Details**: View detailed information including stats, types, abilities, and official artwork
- **Offline Support**: Browse previously loaded Pokemon without internet connection
- **Smooth Animations**: Shared element transitions between screens
- **Modern UI**: Material Design 3 with dynamic theming and edge-to-edge display
- **Loading States**: Custom Lottie animations for loading indicators
- **Error Handling**: Graceful error handling with user-friendly messages

## Architecture

This project follows **Clean Architecture** principles with a multi-module structure, separating concerns and ensuring maintainability, testability, and scalability.

### Why Clean Architecture?

Clean Architecture provides several key benefits:

1. **Independence**: Business logic is independent of frameworks, UI, and external dependencies
2. **Testability**: Core business logic can be tested without UI, database, or network
3. **Maintainability**: Clear separation of concerns makes code easier to understand and modify
4. **Scalability**: Easy to add new features without affecting existing code
5. **Flexibility**: Easy to swap implementations (e.g., changing databases or API clients)

### Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│                    (app, feature modules)                    │
│  • UI Components (Jetpack Compose)                          │
│  • ViewModels                                                │
│  • UI State Management                                       │
└────────────────────────┬────────────────────────────────────┘
                         │ depends on
┌────────────────────────▼────────────────────────────────────┐
│                       Domain Layer                           │
│                    (core:domain module)                      │
│  • Business Logic                                            │
│  • Use Cases                                                 │
│  • Domain Models                                             │
│  • Repository Interfaces (contracts)                         │
└────────────────────────▲────────────────────────────────────┘
                         │ implemented by
┌────────────────────────┴────────────────────────────────────┐
│                        Data Layer                            │
│               (core:data, core:network modules)              │
│  • Repository Implementations                                │
│  • Data Sources (Remote & Local)                             │
│  • Data Mappers                                              │
│  • Caching Strategy                                          │
└─────────────────────────────────────────────────────────────┘
```

### Dependency Rule

Dependencies flow inward:
- **Presentation** depends on **Domain**
- **Data** depends on **Domain**
- **Domain** depends on nothing (pure Kotlin)

This ensures the core business logic remains independent and testable.

## Project Structure

```
Pokemonapp/
├── app/                          # Application module
│   ├── navigation/              # Navigation setup
│   └── MainActivity.kt          # Entry point
│
├── core/
│   ├── data/                    # Data layer
│   │   ├── local/              # Room database
│   │   │   ├── dao/            # Data Access Objects
│   │   │   ├── entity/         # Database entities
│   │   │   └── database/       # Database setup
│   │   ├── paging/             # Paging 3 implementation
│   │   │   └── PokemonRemoteMediator.kt
│   │   ├── mapper/             # Data mappers (DTO ↔ Domain)
│   │   ├── repository/         # Repository implementations
│   │   └── di/                 # Dependency injection
│   │
│   ├── domain/                  # Domain layer (Business logic)
│   │   ├── model/              # Domain models
│   │   ├── repository/         # Repository interfaces
│   │   └── usecase/            # Use cases
│   │       ├── GetPokemonListUseCase.kt
│   │       ├── GetPokemonDetailUseCase.kt
│   │       └── SearchPokemonUseCase.kt
│   │
│   ├── network/                 # Network layer
│   │   ├── api/                # Retrofit API services
│   │   ├── dto/                # Data Transfer Objects
│   │   └── di/                 # Network DI module
│   │
│   ├── design-system/           # Design system
│   │   ├── theme/              # Material 3 theme
│   │   └── components/         # Reusable UI components
│   │
│   └── navigation/              # Navigation contracts
│       └── Screen.kt           # Screen definitions
│
├── feature/
│   ├── home/                    # Home screen feature
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── components/         # Feature-specific components
│   │
│   └── detail/                  # Detail screen feature
│       ├── DetailScreen.kt
│       ├── DetailViewModel.kt
│       └── components/         # Feature-specific components
│
└── gradle/
    └── libs.versions.toml      # Version catalog
```

## Tech Stack

### Core Technologies

- **[Kotlin](https://kotlinlang.org/)** - Modern programming language for Android
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - Modern declarative UI framework
- **[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** - Asynchronous programming
- **[Kotlin Flow](https://kotlinlang.org/docs/flow.html)** - Reactive data streams

### Architecture Components

- **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)** - UI state management
- **[Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)** - Lifecycle-aware components
- **[Room](https://developer.android.com/training/data-storage/room)** - Local database with SQLite abstraction
- **[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)** - Efficient data pagination

### Paging 3 with RemoteMediator

The app uses **Paging 3** library with **RemoteMediator** for efficient data loading:

#### Why Paging 3?

- **Memory Efficiency**: Loads data in chunks, preventing out-of-memory errors
- **Network Efficiency**: Fetches only what's needed, reducing bandwidth usage
- **Smooth Scrolling**: Pre-fetches data before user reaches the end
- **Built-in Loading States**: Automatic handling of loading, error, and empty states

#### RemoteMediator Pattern

The `PokemonRemoteMediator` implements an **offline-first strategy**:

```
┌─────────────┐
│     UI      │ ← Observes PagingData
└──────┬──────┘
       │
┌──────▼──────────────────────────────────────┐
│         Paging 3 Library                    │
│  • Manages pagination logic                 │
│  • Triggers RemoteMediator when needed      │
└──────┬──────────────────────────────────────┘
       │
       ├──────────────┬──────────────┐
       │              │              │
┌──────▼──────┐ ┌────▼─────┐ ┌─────▼──────┐
│    Room     │ │  Remote  │ │  PokeAPI   │
│  Database   │ │ Mediator │ │  (Network) │
│ (Local DB)  │ │          │ │            │
└─────────────┘ └──────────┘ └────────────┘
```

**How it works:**

1. **UI requests data** → Paging 3 checks Room database
2. **If data exists locally** → Display immediately (offline-first)
3. **If data is missing or refresh needed** → RemoteMediator fetches from API
4. **API response** → Saved to Room database
5. **Room database change** → Automatically updates UI via Flow

**Benefits:**

- Offline support - Browse previously loaded Pokemon without internet
- Single source of truth - Database is always the source displayed to UI
- Efficient caching - Reduces API calls and data usage
- Seamless updates - Data automatically refreshes when available

### Dependency Injection

- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** - Dependency injection framework
- **[Hilt Navigation Compose](https://developer.android.com/jetpack/compose/libraries#hilt)** - Hilt integration with Compose Navigation

### Networking

- **[Retrofit](https://square.github.io/retrofit/)** - Type-safe HTTP client
- **[OkHttp](https://square.github.io/okhttp/)** - HTTP client with interceptors
- **[Gson](https://github.com/google/gson)** - JSON serialization/deserialization

### Image Loading

- **[Coil](https://coil-kt.github.io/coil/compose/)** - Image loading library for Compose
- **[Palette](https://developer.android.com/training/material/palette-colors)** - Extract dominant colors from images

### UI/UX

- **[Material Design 3](https://m3.material.io/)** - Modern Material Design components
- **[Lottie](https://github.com/airbnb/lottie-android)** - JSON-based animations
- **[Kotlinx Collections Immutable](https://github.com/Kotlin/kotlinx.collections.immutable)** - Immutable collections for Compose stability

### Testing

- **[JUnit 4](https://junit.org/junit4/)** - Unit testing framework
- **[MockK](https://mockk.io/)** - Mocking library for Kotlin
- **[Truth](https://truth.dev/)** - Fluent assertion library
- **[Turbine](https://github.com/cashapp/turbine)** - Flow testing library
- **[Coroutines Test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)** - Testing utilities for coroutines

### Logging

- **[Timber](https://github.com/JakeWharton/timber)** - Logging utility

### Serialization

- **[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)** - Kotlin-first serialization

## Setup Instructions

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Java 11 or later
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36 (Android 15)
- **Gradle**: 8.13 or later

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone 
   cd pokemonapp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory and open it

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - If not, click "File" → "Sync Project with Gradle Files"

4. **Build the project**
   ```bash
   ./gradlew build
   ```

## Running on a Physical Device

### Enable Developer Options

1. **Open Settings** on your Android device
2. Navigate to **About Phone**
3. Tap **Build Number** 7 times to enable Developer Options
4. Go back to **Settings** → **System** → **Developer Options**
5. Enable **USB Debugging**

### Connect and Run

1. **Connect your device** via USB cable to your computer

2. **Verify device connection**
   ```bash
   adb devices
   ```
   You should see your device listed

3. **Run the app from Android Studio**
   - Click the "Run" button (green play icon) or press `Shift + F10`
   - Select your physical device from the deployment target list
   - Click "OK"

4. **Alternatively, build and install via command line**
   ```bash
   # Debug build
   ./gradlew installDebug
   
   # Release build (requires signing configuration)
   ./gradlew installRelease
   ```

5. **Launch the app** on your device

### Troubleshooting

- **Device not detected**: Check USB cable, try different USB port, or restart ADB
  ```bash
  adb kill-server
  adb start-server
  ```

- **Installation failed**: Uninstall any existing version of the app
  ```bash
  adb uninstall com.robert.pokemonapp
  ```

- **Permission denied**: On your device, allow USB debugging when prompted

## Running Tests

### Unit Tests
```bash
# Run all unit tests
./gradlew test

# Run tests for specific module
./gradlew :core:data:test
./gradlew :core:domain:test
```

### Instrumented Tests (Android Tests)
```bash
# Run all instrumented tests
./gradlew connectedAndroidTest

# Run tests for specific module
./gradlew :core:data:connectedDebugAndroidTest
```

### Test Coverage
```bash
./gradlew testDebugUnitTestCoverage
```

## Key Features Implementation

### 1. Offline-First Architecture

The app prioritizes local data:
- All Pokemon data is cached in Room database
- UI always reads from local database
- Network calls only refresh the cache
- Works completely offline after initial data load

### 2. Efficient Pagination

- Loads 20 Pokemon at a time
- Pre-fetches next page before scrolling ends
- Maintains scroll position across configuration changes
- Handles loading, error, and empty states

### 3. Search Functionality

- Local-first search in cached Pokemon
- Falls back to API if not found locally
- Debounced search to reduce API calls
- Displays user-friendly error messages

### 4. Material Design 3

- Dynamic color theming
- Adaptive layouts
- Smooth animations and transitions
- Edge-to-edge display

## Design Patterns Used

- **MVVM** (Model-View-ViewModel) - Separation of UI and business logic
- **Repository Pattern** - Abstraction of data sources
- **Use Case Pattern** - Encapsulation of business logic
- **Dependency Injection** - Loose coupling and testability
- **Observer Pattern** - Reactive UI updates with Flow
- **Mapper Pattern** - Data transformation between layers
- **Single Source of Truth** - Database as the authoritative data source

## API Reference

This app uses the [PokeAPI](https://pokeapi.co/):
- **Base URL**: `https://pokeapi.co/api/v2/`
- **Endpoints**:
  - `GET /pokemon?limit={limit}&offset={offset}` - List Pokemon
  - `GET /pokemon/{id or name}` - Get Pokemon details

## App Icon

The app features a custom **Pokeball-themed** adaptive icon:

- **Design**: Classic Pokeball design with red top, white bottom, and black center band
- **Foreground**: Central button with white border and black inner circle
- **Format**: Vector drawable (scalable to any size)
- **Compatibility**: Adaptive icon (Android 8.0+) with fallback WebP images for older versions

The icon is instantly recognizable and represents the Pokemon theme perfectly, making it easy to find on your device.

## Contributing


