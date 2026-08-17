package com.miva.billmanager.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miva.billmanager.R
import com.miva.billmanager.domain.usecase.*
import com.miva.billmanager.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val analyzeBillUseCase: AnalyzeBillUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        combine(
            getExpensesUseCase(),
            _state.map { it.startDate }.distinctUntilChanged(),
            _state.map { it.endDate }.distinctUntilChanged()
        ) { expenses, start, end ->
            val filtered = expenses.filter { it.date in start..end }
            val total = filtered.sumOf { it.amount }
            Triple(expenses, total, filtered)
        }.onEach { (all, total, _) ->
            _state.update { it.copy(expenses = all, totalAmount = total) }
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.OpenCamera -> _state.update { it.copy(isCameraOpen = true) }
            DashboardIntent.CloseCamera -> _state.update { it.copy(isCameraOpen = false) }
            is DashboardIntent.CaptureBill -> analyzeBill(intent.bitmap)
            is DashboardIntent.AddManualExpense -> {
                viewModelScope.launch {
                    addExpenseUseCase(intent.expense)
                    _effect.emit(DashboardEffect.ShowToast(UiText.StringResource(R.string.expense_added_manually)))
                }
            }
            is DashboardIntent.UpdateExpense -> {
                viewModelScope.launch {
                    updateExpenseUseCase(intent.expense)
                    _effect.emit(DashboardEffect.ShowToast(UiText.StringResource(R.string.expense_updated)))
                }
            }
            is DashboardIntent.DeleteExpense -> {
                viewModelScope.launch {
                    _state.value.expenses.find { it.id == intent.id }?.let {
                        deleteExpenseUseCase(it)
                        _effect.emit(DashboardEffect.ShowToast(UiText.StringResource(R.string.expense_deleted)))
                    }
                }
            }
            is DashboardIntent.SetFilterDates -> {
                _state.update { it.copy(startDate = intent.start, endDate = intent.end) }
            }
            DashboardIntent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun analyzeBill(bitmap: android.graphics.Bitmap) {
        _state.update { it.copy(isLoading = true, isCameraOpen = false) }
        viewModelScope.launch {
            analyzeBillUseCase(bitmap)
                .onSuccess { expense ->
                    addExpenseUseCase(expense)
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(DashboardEffect.ShowToast(UiText.StringResource(R.string.bill_analyzed_added)))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.localizedMessage) }
                }
        }
    }
}
