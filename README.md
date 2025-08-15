# Explorer App

Explorer is a modern Android application that demonstrates contemporary Android development practices. It allows users to search for countries and view detailed information using the [RestCountries API](https://restcountries.com/).

## 🌟 Features

*   **🔍 Country Search:** Search for countries by name with real-time debounced input
*   **🏳️ Country Listing:** View search results with country flags and official names
*   **⚡ Reactive UI:** Loading states and error handling with smooth user experience
*   **🏗️ Clean Architecture:** Built with MVI (Model-View-Intent) pattern for maintainable code
*   **📱 Modern UI:** Edge-to-edge design with Material 3 components
*   **🔧 Custom Logging:** Centralized logging system for better debugging

## 🛠️ Technologies & Libraries Used

*   **Kotlin 2.0.21:** Primary programming language with latest features
*   **Jetpack Compose:** Declarative UI toolkit with Material 3 design
*   **Coroutines & Flow:** Asynchronous programming and reactive data streams
*   **Hilt/Dagger:** Dependency injection framework
*   **Retrofit 2.9.0:** Type-safe HTTP client for API communication
*   **Kotlinx Serialization:** Modern JSON parsing and serialization
*   **ViewModel:** Lifecycle-aware UI state management
*   **Navigation Compose:** Type-safe navigation between screens
*   **MVI Architecture:** Unidirectional data flow pattern
*   **Detekt:** Static code analysis for code quality
*   **Mockito & Turbine:** Comprehensive testing framework

## 🚀 Setup & Installation

### Prerequisites
*   **Android Studio:** Iguana (2023.2.1) or newer
*   **Android SDK:** API level 24 (Android 7.0) or higher
*   **Java:** JDK 11 or higher

### Installation Steps
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/Explorer.git
    cd Explorer
    ```
2.  **Open in Android Studio:**
    Open the project in Android Studio and let it sync dependencies automatically.
3.  **Build and Run:**
    ```bash
    ./gradlew assembleDebug
    ```
    Or use Android Studio's "Run" button to build and install on an emulator or device.

## 🏗️ Project Architecture

### Clean Architecture Layers
```
app/src/main/java/com/ferrarib/explorer/
├── 🎯 presentation/     # UI Layer (Compose + ViewModels)
│   ├── home/           # Home screen with navigation menu
│   ├── search/         # Country search feature
│   └── theme/          # Material 3 theme configuration
├── 📊 data/            # Data Layer
│   └── repository/     # Repository implementations
├── 🔧 core/            # Core Infrastructure
│   ├── data/          # Network models and API definitions
│   ├── di/            # Dependency injection modules
│   ├── ui/            # Reusable UI components
│   └── utils/         # Utilities (AppLogger, etc.)
├── 🏛️ domain/          # Business Logic Layer
└── 🧭 navigation/      # App navigation setup
```

### Key Components
*   **MVI Pattern:** Unidirectional data flow with State, Action, and Effect
*   **Repository Pattern:** Centralized data access and caching
*   **Dependency Injection:** Hilt modules for clean separation of concerns
*   **Custom Components:** Reusable UI components (AppBar, MenuButton)
*   **Debounced Search:** Performance-optimized search with 800ms delay

## 🧪 Testing

The project includes comprehensive testing:

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run static analysis with Detekt
./gradlew detekt
```

### Test Coverage
*   **Unit Tests:** ViewModel logic with Mockito and Turbine
*   **UI Tests:** Compose UI testing with Espresso
*   **Integration Tests:** Repository and API integration
*   **Static Analysis:** Code quality checks with Detekt

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1.  **Fork the Project**
2.  **Create a Feature Branch:** `git checkout -b feature/amazing-feature`
3.  **Run Tests:** Ensure all tests pass before committing
4.  **Commit Changes:** `git commit -m 'Add amazing feature'`
5.  **Push to Branch:** `git push origin feature/amazing-feature`
6.  **Open a Pull Request**

### Code Style
*   Follow Kotlin coding conventions
*   Use Detekt for static analysis
*   Write tests for new features
*   Update documentation as needed

## 📱 Screenshots

### Current Features
*   **Home Screen:** Simple navigation menu to access app features
*   **Search Screen:** Real-time country search with flag display
*   **Results List:** Clean listing with country flags and official names
*   **Loading States:** Smooth loading indicators and error handling

## 🎯 Future Enhancements

*   **Country Details:** Detailed view with population, area, currencies, etc.
*   **Offline Support:** Cache frequently accessed countries
*   **Favorites:** Save favorite countries for quick access
*   **Maps Integration:** Show country location on interactive maps
*   **Dark Theme:** Complete dark mode support
*   **Accessibility:** Enhanced screen reader and accessibility support

## 📄 License

This project is distributed under the terms of the license. See the [LICENSE](LICENSE) file for more details.

---