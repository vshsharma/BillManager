package com.miva.billmanager.domain.usecase

import com.miva.billmanager.domain.model.Expense
import com.miva.billmanager.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> = repository.getAllExpenses()
}
