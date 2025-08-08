# Movies App KMM 🎬

A modern cross-platform movie discovery app built with **Kotlin Multiplatform Mobile (KMM)** and **Compose Multiplatform**. Browse popular movies, view detailed information, and enjoy a seamless experience across Android and iOS platforms.

## 📱 Features

- **Cross-Platform**: Single codebase for Android and iOS
- **Movie Discovery**: Browse popular movies with rich details
- **Movie Details**: View comprehensive information about each movie
- **Modern UI**: Beautiful Material Design 3 interface
- **Image Loading**: Optimized image loading with Coil
- **Splash Screen**: Branded app launch experience
- **Responsive Design**: Adaptive layouts for different screen sizes

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin Multiplatform Mobile (KMM)** - Share business logic across platforms
- **Compose Multiplatform** - Unified UI framework
- **Material Design 3** - Modern design system

### Architecture & Libraries
- **MVVM Pattern** - Clean architecture with ViewModels
- **Koin** - Dependency injection
- **Ktor** - Network client for API calls
- **Kotlinx Serialization** - JSON parsing
- **Kotlinx Coroutines** - Asynchronous programming
- **Coil** - Image loading library
- **Jetpack Navigation** - Navigation component

### Platform-Specific
- **Android**: Activity Compose, Android Gradle Plugin 8.6.0
- **iOS**: UIKit integration, Xcode framework

## 🏗️ Project Structure

```
MoviesAppKmm/
├── androidApp/                 # Android-specific code
│   ├── src/main/java/         # Android MainActivity and theme
│   └── build.gradle.kts       # Android build configuration
├── iosApp/                    # iOS-specific code
│   ├── iosApp/               # SwiftUI integration
│   └── iosApp.xcodeproj/     # Xcode project
├── shared/                    # Shared multiplatform code
│   ├── src/
│   │   ├── commonMain/       # Shared business logic
│   │   │   ├── kotlin/com/dev/moviesappkmm/
│   │   │   │   ├── data/     # Data models and repository
│   │   │   │   ├── network/  # API service and client
│   │   │   │   ├── presentation/ # ViewModels and UI
│   │   │   │   └── module/   # Dependency injection
│   │   ├── androidMain/      # Android-specific implementations
│   │   └── iosMain/          # iOS-specific implementations
│   └── build.gradle.kts      # Shared module configuration
├── gradle/
│   └── libs.versions.toml    # Version catalog
└── build.gradle.kts          # Root build configuration
```

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Flamingo or newer)
- **Xcode** 14+ (for iOS development)
- **JDK** 17 or higher
- **Kotlin** 2.1.20+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/MoviesAppKmm.git
   cd MoviesAppKmm
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **API Configuration**
   - Obtain an API key from [The Movie Database (TMDb)](https://www.themoviedb.org/settings/api)
   - Open `local.properties` file in the root directory
   - Add your API key:
     ```properties
     tmdb.api.key=your_actual_api_key_here
     ```
   - For iOS builds, also update the API key in:
     `shared/src/iosMain/kotlin/com/dev/moviesappkmm/config/ApiConfig.ios.kt`

   **⚠️ Important**: Never commit your API key to version control. The `local.properties` file is already in `.gitignore`.

### Running the App

#### Android
```bash
./gradlew :androidApp:installDebug
```

#### iOS
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or device
3. Press ▶️ to run

## 📦 Dependencies

### Shared Module
- Compose Multiplatform 1.8.2
- Ktor 3.1.0
- Koin 4.0.0
- Kotlinx Coroutines 1.7.3
- Coil 3.2.0
- Navigation Compose 2.8.0

### Version Catalog
All dependencies are managed through Gradle's version catalog (`gradle/libs.versions.toml`) for consistent versioning across modules.

## 🎨 UI Components

- **Splash Screen**: Animated launch screen with app branding
- **Home Screen**: Grid layout of popular movies with poster images
- **Movie Detail Screen**: Comprehensive movie information display
- **Navigation**: Smooth transitions between screens

## 🏛️ Architecture

The app follows Clean Architecture principles:

- **Data Layer**: Repository pattern with API service
- **Domain Layer**: Use cases for business logic
- **Presentation Layer**: ViewModels and Compose UI

### Key Components

- `MovieRepository`: Handles data operations
- `MovieApiService`: Network API interface
- `MoviesViewModel`: UI state management
- `GetPopularMoviesUseCase`: Business logic encapsulation

## 🔧 Configuration

### Gradle
- Android Gradle Plugin: 8.6.0
- Kotlin: 2.1.20
- Target SDK: 35
- Min SDK: 24

### iOS Framework
The shared module exports an iOS framework with proper bundle configuration for seamless integration.

## 🚨 Troubleshooting

Common issues and solutions are documented in [TROUBLESHOOTING_GUIDE.md](TROUBLESHOOTING_GUIDE.md).

### Common Issues
- **Navigation dependency conflicts**: Ensure proper version alignment
- **iOS build cache issues**: Clean build folder if needed
- **Gradle sync problems**: Check version catalog consistency

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [The Movie Database (TMDb)](https://www.themoviedb.org/) for providing the movie API
- [JetBrains](https://www.jetbrains.com/) for Kotlin Multiplatform and Compose Multiplatform
- [Square](https://square.github.io/) for open-source libraries

## 📞 Contact

- **Author**: Your Name
- **Email**: your.email@example.com
- **Project Link**: [https://github.com/yourusername/MoviesAppKmm](https://github.com/yourusername/MoviesAppKmm)

---

Built with ❤️ using Kotlin Multiplatform Mobile
