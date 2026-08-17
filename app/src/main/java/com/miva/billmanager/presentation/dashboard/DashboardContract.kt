package com.miva.billmanager.presentation.dashboard

import android.graphics.Bitmap
import com.miva.billmanager.domain.model.Expense
import com.miva.billmanager.ui.util.UiText

data class DashboardState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Double = 0.0,
    val startDate: String = "2026-08-01",
    val endDate: String = "2026-08-31",
    val isLoading: Boolean = false,
    val isCameraOpen: Boolean = false,
    val error: String? = null
)

sealed class DashboardIntent {
    data object OpenCamera : DashboardIntent()
    data object CloseCamera : DashboardIntent()
    data class CaptureBill(val bitmap: Bitmap) : DashboardIntent()
    data class AddManualExpense(val expense: Expense) : DashboardIntent()
    data class UpdateExpense(val expense: Expense) : DashboardIntent()
    data class DeleteExpense(val id: String) : DashboardIntent()
    data class SetFilterDates(val start: String, val end: String) : DashboardIntent()
    data object ClearError : DashboardIntent()
}

sealed class DashboardEffect {
    data class ShowToast(val message: UiText) : DashboardEffect()
}
