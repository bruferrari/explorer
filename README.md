# Explorer App

Explorer is an Android application built to demonstrate modern Android development practices. It allows users to search for and explore information about countries using the [RestCountries API](https://restcountries.com/).

## 🌟 Features

*   **Country Search:** Users can search for countries by name.
*   **Country Details:** (Implied - you would typically display details after finding a country)
*   Built with a focus on a clean, scalable MVI (Model-View-Intent) architecture.
*   Utilizes modern Android Jetpack libraries.

## 🛠️ Technologies & Libraries Used

*   **Kotlin:** Primary programming language.
*   **Jetpack Compose:** For building the UI declaratively.
*   **Coroutines & Flow:** For asynchronous programming and reactive data streams.
*   **Hilt:** For dependency injection.
*   **Retrofit:** For type-safe HTTP client and API communication.
*   **Kotlinx Serialization:** For JSON parsing.
*   **ViewModel:** For managing UI-related data in a lifecycle-conscious way.
*   **Navigation Compose:** For navigating between screens.
*   **MVI Architecture:** (Model-View-Intent) A unidirectional data flow pattern.

## 🚀 Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/Explorer.git
    ```
    (Remember to replace `YOUR_USERNAME` with your actual GitHub username after you create the repository)
2.  **Open in Android Studio:**
    Open the cloned project in Android Studio (Arctic Fox or newer recommended).
3.  **Build the project:**
    Android Studio should automatically sync and download the necessary dependencies. Click the "Build" button (or "Run") to build and install the app on an emulator or a connected device.

## 🏗️ Project Structure (Simplified)

*   `app/src/main/java/com/ferrarib/explorer/`
    *   `core/`: Core components like base classes (e.g., `ModelViewIntent`), networking (`HttpClient`, `ExplorerApi`), DI modules (`NetworkModule`).
    *   `data/`: Data layer components like repositories (`ExplorerRepository`).
    *   `domain/`: Domain models (e.g., `Country`).
    *   `presentation/`: UI layer components (Composables, ViewModels).
        *   `search/`: Feature-specific UI and ViewModel for country search.
        *   `theme/`: Compose theme (Color, Type, Theme).
    *   `MainActivity.kt`: The main entry point of the application.
*   `app/src/main/AndroidManifest.xml`: Android application manifest.
*   `build.gradle.kts` (project and app level): Build scripts for Gradle.
*   `gradle/libs.versions.toml`: Version catalog for dependencies.

## 🤝 Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and create a pull request. You can also open an issue if you find a bug or have a feature suggestion.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m '''Add some AmazingFeature'''`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📄 License

This project is currently not licensed. You should add a `LICENSE` file (e.g., MIT, Apache 2.0) to specify how others can use your code.

---

_This README was generated to help get you started. Feel free to customize it further!_