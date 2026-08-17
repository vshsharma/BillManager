# Bill Manager - Clean Architecture & MVI

This project is built using modern Android development practices, following **Clean Architecture** principles and the **MVI (Model-View-Intent)** presentation pattern.

## Architecture Overview

The app is divided into three main layers:

### 1. Domain Layer
The core of the application. It contains the business logic and is completely independent of any frameworks, UI, or databases.
- **Model**: `Expense` represents the pure business entity.
- **Repository Interface**: `ExpenseRepository` defines the contract for data operations.
- **Use Cases**: Individual classes like `GetExpensesUseCase` and `AnalyzeBillUseCase` that perform a single business action. This makes the logic reusable and testable.

### 2. Data Layer
Handles data persistence and external integrations.
- **Local**: Room database implementation with `AppDatabase`, `ExpenseDao`, and `ExpenseEntity`.
- **Repository Implementation**: `ExpenseRepositoryImpl` implements the domain interface, coordinating between the Room database and the Gemini AI model.
- **Mapper**: `ExpenseMapper` ensures that data entities remain isolated from business models.
- **AI**: `AIModelManager` configures the Gemini 3.7 Flash model for bill parsing.

### 3. Presentation Layer
Implements the **MVI** pattern for a reactive and predictable UI.
- **Contract**: `DashboardContract` defines the `State` (what the UI shows), `Intent` (what the user does), and `Effect` (one-time events like Toasts).
- **ViewModel**: `DashboardViewModel` processes Intents, interacts with Use Cases, and reduces the results into a new State.
- **UI**: Jetpack Compose based `DashboardScreen` and components. It observes the state and dispatches intents.

## Why this Architecture?

- **Testability**: Use Cases and Repositories can be unit tested in isolation.
- **Scalability**: New features can be added by creating new Use Cases without bloating the ViewModel.
- **Predictability**: MVI ensures a unidirectional data flow, making state management easier to debug.
- **Separation of Concerns**: UI doesn't know about the database, and the database doesn't know about Gemini.

## Key Technologies
- **Hilt**: Dependency Injection.
- **Room**: Local SQL persistence.
- **Gemini AI**: Automated bill parsing.
- **CameraX**: Bill photo capture.
- **Jetpack Compose**: Modern UI toolkit.

## Project Structure
```
com.miva.billmanager
├── data            # Room, Mappers, Repos Implementation
├── di              # Hilt Modules
├── domain          # Business Models, Use Cases, Repos Interfaces
└── presentation    # MVI Contract, ViewModel, Screen, Components
```
