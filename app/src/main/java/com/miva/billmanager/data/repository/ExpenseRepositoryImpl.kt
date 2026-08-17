package com.miva.billmanager.data.repository

import android.graphics.Bitmap
import com.google.firebase.ai.type.content
import com.miva.billmanager.AIModelManager
import com.miva.billmanager.data.local.dao.ExpenseDao
import com.miva.billmanager.data.mapper.toDomain
import com.miva.billmanager.data.mapper.toEntity
import com.miva.billmanager.domain.model.Expense
import com.miva.billmanager.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }

    override suspend fun analyzeBill(bitmap: Bitmap): Result<Expense> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Analyze this bill and extract the following information in JSON format:
                {
                  "title": "Merchant or main item",
                  "amount": 0.0,
                  "category": "Food, Transport, Home, Bills, or Others",
                  "date": "YYYY-MM-DD",
                  "notes": "A brief description or additional info"
                }
                Return ONLY the JSON object.
            """.trimIndent()

            val response = AIModelManager.generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )

            val outputContent = response.text ?: return@withContext Result.failure(Exception("No content received from AI"))
            
            val jsonStr = outputContent.substringAfter("{").substringBeforeLast("}")
            val json = JSONObject("{$jsonStr}")

            val expense = Expense(
                id = UUID.randomUUID().toString(),
                title = json.optString("title", "Unknown"),
                amount = json.optDouble("amount", 0.0),
                category = json.optString("category", "Others"),
                date = json.optString("date", ""),
                notes = json.optString("notes", "")
            )
            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
