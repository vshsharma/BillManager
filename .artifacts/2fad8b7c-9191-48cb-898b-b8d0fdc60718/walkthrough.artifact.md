# Walkthrough - Clean Architecture & MVI Refactor

I have completely refactored the Bill Manager app to follow **Clean Architecture** principles and the **MVI (Model-View-Intent)** presentation pattern. This structure ensures the app is scalable, testable, and maintainable.

## Architectural Changes

### 1. Domain Layer (The Core)
This layer contains the pure business logic, isolated from Android and external libraries.
- **[Expense.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/domain/model/Expense.kt)**: The pure business model.
- **[ExpenseRepository.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/domain/repository/ExpenseRepository.kt)**: Defines the contract for data operations.
- **Use Cases**: Each class performs a single action (e.g., `AddExpenseUseCase`, `AnalyzeBillUseCase`), making the logic reusable and easy to test.

### 2. Data Layer (Implementation)
Handles persistence and external integrations (Room & Gemini).
- **[ExpenseEntity.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/data/local/entity/ExpenseEntity.kt)**: The database model.
- **[ExpenseRepositoryImpl.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/data/repository/ExpenseRepositoryImpl.kt)**: Orchestrates data between the local DB and the AI model.
- **[ExpenseMapper.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/data/mapper/ExpenseMapper.kt)**: Maps between Entity and Domain models to keep layers decoupled.

### 3. Presentation Layer (MVI)
A reactive UI pattern with unidirectional data flow.
- **[DashboardContract.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/presentation/dashboard/DashboardContract.kt)**: Centralizes `State`, `Intent`, and `Effect`.
- **[DashboardViewModel.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/presentation/dashboard/DashboardViewModel.kt)**: Processes Intents from the UI and updates the State via Use Cases.
- **[DashboardScreen.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/presentation/dashboard/DashboardScreen.kt)**: A stateless-like Composable that observes State and dispatches Intents.

## Best Practices & Cleanup

### Resource Extraction
- **[strings.xml](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/res/values/strings.xml)**: Moved all hardcoded strings (UI labels, Toasts, prompts) to the resource file.
- **[UiText.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/ui/util/UiText.kt)**: Implemented a helper to handle string resources and dynamic strings safely in the ViewModel.

### Configuration & Constants
- **[AIModelManager.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/AIModelManager.kt)**: Moved Gemini configuration parameters (model name, temperature, topK) to a `companion object` as requested.

### Dependency Injection
- **[DataModule.kt](file:///Users/vishal/Documents/workspace/study/BillManager/app/src/main/java/com/miva/billmanager/di/DataModule.kt)**: Set up Hilt to provide database, DAO, and repository instances correctly.

## Verification
- **Gradle Sync**: Successful.
- **Build Success**: `app:assembleDebug` completed successfully.
- **Functionality**: The app maintains all features (AI scanning, manual entry, delete confirmation, total summary) while using the new robust architecture.

> [!TIP]
> Refer to the new **[README.md](file:///Users/vishal/Documents/workspace/study/BillManager/README.md)** at the root for a detailed explanation of why each file exists and how the layers interact.
