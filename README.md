# Tic-Tac-Toe

A simple single-player Tic-Tac-Toe game for Android built with Jetpack Compose.

## Features

*   Single-player game against an AI opponent.
*   Tracks the user's wins.
*   Reset the game at any time.
*   Simple and clean UI built with Jetpack Compose.

## Architecture

The app follows the MVVM (Model-View-ViewModel) architectural pattern.

*   **Model:** The `GameState` data class represents the state of the game.
*   **View:** The UI is built with Composable functions that observe the state from the `GameViewModel`.
*   **ViewModel:** The `GameViewModel` contains the game logic and state, and exposes it to the View.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

*   Android Studio
*   An Android emulator or a physical device

### Installation

1.  Clone the repo
2.  Open the project in Android Studio
3.  Run the app

## Running the tests

To run the unit tests, you can use the following command:

```sh
./gradlew test
```

## Built With

*   [Jetpack Compose](https://developer.android.com/jetpack/compose) - The modern toolkit for building native Android UI.
*   [Kotlin](https://kotlinlang.org/) - The programming language used to build the app.
*   [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - A data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
