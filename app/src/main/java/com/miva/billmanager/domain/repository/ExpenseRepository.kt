package com.miva.billmanager.domain.repository

import android.graphics.Bitmap
import com.miva.billmanager.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun analyzeBill(bitmap: Bitmap): Result<Expense>
}
