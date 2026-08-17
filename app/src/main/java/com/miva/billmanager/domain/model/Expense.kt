package com.miva.billmanager.domain.model

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val notes: String = ""
)
