package com.miva.billmanager.domain.usecase

import com.miva.billmanager.domain.model.Expense
import com.miva.billmanager.domain.repository.ExpenseRepository
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.updateExpense(expense)
}
