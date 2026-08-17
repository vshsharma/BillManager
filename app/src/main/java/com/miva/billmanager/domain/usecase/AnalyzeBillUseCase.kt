package com.miva.billmanager.domain.usecase

import android.graphics.Bitmap
import com.miva.billmanager.domain.repository.ExpenseRepository
import javax.inject.Inject

class AnalyzeBillUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(bitmap: Bitmap) = repository.analyzeBill(bitmap)
}
