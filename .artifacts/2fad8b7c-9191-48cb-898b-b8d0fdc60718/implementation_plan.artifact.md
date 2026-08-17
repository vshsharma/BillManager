# Implementation Plan - Clean Architecture & MVI Refactor

Refactor the BillManager app to follow Clean Architecture principles and the MVI (Model-View-Intent) presentation pattern.

## Proposed Changes

### 1. Domain Layer
Create the core business logic and rules, independent of any frameworks.
- **[NEW]** `com.miva.billmanager.domain.model.Expense`: Move the data class here.
- **[NEW]** `com.miva.billmanager.domain.repository.ExpenseRepository`: Interface for data operations.
- **[NEW]** `com.miva.billmanager.domain.usecase`:
    - `GetExpensesUseCase`: Returns a Flow of expenses.
    - `AddExpenseUseCase`: Adds a manual or scanned expense.
    - `UpdateExpenseUseCase`: Updates an existing expense.
    - `DeleteExpenseUseCase`: Deletes an expense.
    - `AnalyzeBillUseCase`: Uses AI to extract data from a bitmap.

### 2. Data Layer
Implement the repository and handle data sources (Room & Gemini).
- **[NEW]** `com.miva.billmanager.data.local.entity.ExpenseEntity`: Database entity.
- **[NEW]** `com.miva.billmanager.data.local.dao.ExpenseDao`: Room DAO.
- **[NEW]** `com.miva.billmanager.data.local.AppDatabase`: Room Database.
- **[NEW]** `com.miva.billmanager.data.repository.ExpenseRepositoryImpl`: Implementation of the domain repository.
- **[NEW]** `com.miva.billmanager.data.mapper.ExpenseMapper`: Conversion between Entity and Domain model.
- **[MODIFY]** `com.miva.billmanager.AIModelManager`: Keep as a helper for Gemini configuration in the data layer.

### 3. Presentation Layer (MVI)
Handle UI state and user interactions using a reactive flow.
- **[NEW]** `com.miva.billmanager.presentation.dashboard.DashboardContract`:
    - `DashboardState`: Data class representing the UI state (expenses, loading, camera visibility, etc.).
    - `DashboardIntent`: Sealed class for user actions (Scan, Capture, Add, Edit, Delete, Filter).
    - `DashboardEffect`: Sealed class for one-time events (Toasts, navigation).
- **[MODIFY]** `com.miva.billmanager.presentation.dashboard.DashboardViewModel`:
    - Orchestrate Use Cases.
    - Handle `DashboardIntent` and reduce it to `DashboardState`.
- **[MODIFY]** `com.miva.billmanager.presentation.dashboard.DashboardScreen`:
    - Observe `DashboardState`.
    - Dispatch `DashboardIntent` on user actions.

### 4. Dependency Injection (Hilt)
- **[MODIFY]** `com.miva.billmanager.di`:
    - `DataModule`: Provide Database and Repository implementations.
    - `DomainModule`: Provide Use Case instances.

### 5. Cleanup
- **[DELETE]** Unused files: `BakingViewModel.kt`, `BakingScreen.kt`, `UiState.kt`.
- **[MOVE]** `CameraPreview.kt` to `presentation.dashboard.components`.

### 6. Resource Management & Best Practices
- **[MODIFY]** `res/values/strings.xml`: Extract all hardcoded strings from Composables and ViewModels.
- **[MODIFY]** Move configuration constants (AI prompts, date formats, logic-specific values) to `companion object` blocks in their respective classes.

### 7. Documentation
- **[NEW]** `README.md`: Explain the Clean Architecture layers, MVI flow, and file organization.

## Verification Plan

### Automated Tests
- Build the project to ensure Hilt and Room code generation are correct.

### Manual Verification
1. Verify that scanning a bill still works and auto-adds to the list.
2. Verify that manual adding, editing, and deleting work correctly.
3. Verify that the "Total Spending" glance updates reactively.
4. Ensure persistence is maintained after the refactor.
