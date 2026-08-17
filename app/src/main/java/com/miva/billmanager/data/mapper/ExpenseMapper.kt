package com.miva.billmanager.data.mapper

import com.miva.billmanager.data.local.entity.ExpenseEntity
import com.miva.billmanager.domain.model.Expense

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        date = date,
        category = category,
        notes = notes
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        date = date,
        category = category,
        notes = notes
    )
}
