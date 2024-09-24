---

# My YouTube Playlist App

This is a Jetpack Compose-based Android application that allows users to view and interact with YouTube playlists. The app displays a list of YouTube videos with thumbnails, titles, and other information, allowing users to load more videos as they scroll and navigate to video details pages with a built-in video player.

## Features

- Display YouTube playlist videos with thumbnails, video titles, owner names, and upload dates.
- Implement lazy loading to load more videos when scrolling to the bottom.
- View video details with a YouTube video player on the video player page.
- Show skeleton loading view (placeholder) while the content is being loaded.
- Navigation between video list and video player using Jetpack Navigation.
- Formatted video upload dates in the `YYYY-MM-DD HH:mm:ss` format.

## Screenshots

<!-- Include screenshots of the app's key UI screens, such as the video list and video player screen. -->
![Video List](path_to_screenshot1)
![Video Player](path_to_screenshot2)

## Technologies and Libraries Used

- **Jetpack Compose**: Android's modern UI toolkit for building native UIs.
- **Kotlin**: The programming language used for Android development.
- **Retrofit**: For making network requests to the YouTube Data API.
- **Coil**: For loading and displaying images from the internet (thumbnails and avatars).
- **Jetpack Navigation**: For handling in-app navigation between different screens.
- **YouTube WebView Integration**: To display and play YouTube videos.

## Installation and Setup

### Prerequisites

- Android Studio (latest version recommended)
- Android device or emulator with internet access
- A YouTube Data API key for accessing YouTube playlists

### Steps to Install

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
   ```

2. **Open the project in Android Studio**.

3. **Build and run the project**:
   - Connect an Android device or start an Android emulator.
   - Click `Run` in Android Studio to install and run the app on your device or emulator.

## Usage

1. The app will load a YouTube playlist and display a list of videos.
2. You can scroll down to load more videos.
3. Clicking on a video will navigate to the video player screen, where you can view and play the video.
4. The app will show a skeleton view while loading videos, providing a smooth user experience.

## Code Structure

- **MainActivity.kt**: The entry point of the app, sets up the navigation and UI components.
- - **HomeScreen**: Displays the video list and handles lazy loading.
- **VideoPlayerScreen.kt**: Displays the video player with video details.
- - **SkeletonView**: Handles the placeholder view while the videos are loading.
- **VideoData.kt**: Defines the `VideoData` model that represents the video items.


## Testing

The app includes both unit tests and UI tests:

- **Unit tests**:
  - Located in `app/src/test/java/com/example/YourApp/ExampleUnitTest.kt`.
  - Test the business logic, including `VideoData` initialization and date formatting.

- **Instrumented tests**:
  - Located in `app/src/androidTest/java/com/example/YourApp/ExampleInstrumentedTest.kt`.
  - Test UI components such as skeleton view, lazy loading, and navigation.

### Running the Tests

1. **Unit tests**:
   - Run unit tests in the `ExampleUnitTest.kt` file using the `test` directory in Android Studio.

2. **UI tests**:
   - Run UI tests in the `ExampleInstrumentedTest.kt` file using the `androidTest` directory in Android Studio.

## License

This project is licensed under the MIT License.
