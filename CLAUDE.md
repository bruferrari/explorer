# Explorer Android Project Memory

## Project Overview
Modern Android application demonstrating contemporary development practices. Users can search for countries and view detailed information using the RestCountries API.

### Key Technologies
- **Kotlin 2.0.21** with latest features
- **Jetpack Compose** with Material 3 design
- **MVI Architecture** (Model-View-Intent pattern)
- **Hilt/Dagger** for dependency injection
- **Retrofit** for API communication
- **Kotlinx Serialization** for JSON parsing
- **Coroutines & Flow** for reactive programming

## Common Commands

### Build & Test
```bash
# Build the project
./gradlew build

# Run unit tests
./gradlew test

# Compile Kotlin only
./gradlew compileDebugKotlin

# Run static analysis
./gradlew detekt

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Project Architecture

### Clean Architecture Layers
```
├── presentation/       # UI Layer (Compose + ViewModels)
│   ├── home/          # Home screen
│   ├── search/        # Country search feature
│   └── countrydetails/ # Detailed country view
├── data/              # Data Layer
│   └── repository/    # Repository implementations
├── core/              # Core Infrastructure
│   ├── data/         # Network models and API
│   ├── di/           # Dependency injection
│   ├── ui/           # Reusable UI components
│   │   └── theme/    # Material 3 theme (Color, Theme, Type)
│   └── utils/        # Utilities (AppLogger, etc.)
├── navigation/        # App navigation setup
└── domain/           # Business Logic Layer
```

### Key Architectural Decisions
- **Theme Location**: Moved from `presentation/theme` to `core/ui/theme` for better organization
- **MVI Pattern**: ViewModels use State, Action, and Effect sealed interfaces
- **Automatic Logging**: AppLogger with automatic tag generation from calling class
- **Full Text Search**: CountryDetailsViewModel handles detailed country information

## Code Style & Conventions

### Kotlin Standards
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Prefer immutable data structures

### Compose Guidelines
- Extract reusable composables (e.g., CountryInfoItem)
- Use consistent padding (16.dp for content)
- Create previews for composables

### Testing Strategy
- **Unit Tests**: ViewModel logic with Mockito and Turbine
- **Separate Test Files**: Each ViewModel has its own test file
- **Comprehensive Coverage**: Test success, error, and edge cases

## Recent Changes
- Moved theme components from `presentation/theme` to `core/ui/theme`
- Implemented CountryDetailsViewModel with full text search
- Enhanced AppLogger with automatic tagging
- Added CountryInfoItem composable for reusable UI
- Created comprehensive test coverage for all ViewModels
- Implemented search result clearing when returning from details screen
- Added automatic TextField focus functionality to SearchCountryScreen
- Used LaunchedEffect with FocusRequester for seamless UX improvements

## API Integration
- **Base URL**: https://restcountries.com/v3.1/
- **Endpoints**:
  - `/name/{name}` - Country search
  - `/name/{name}?fullText=true` - Full text search
- **Error Handling**: Repository layer catches exceptions and logs errors

## Dependencies Management
- Version catalog in `gradle/libs.versions.toml`
- Centralized dependency management
- Regular updates to latest stable versions