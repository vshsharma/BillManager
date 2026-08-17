package com.miva.billmanager.di

import android.content.Context
import androidx.room.Room
import com.miva.billmanager.data.local.AppDatabase
import com.miva.billmanager.data.local.dao.ExpenseDao
import com.miva.billmanager.data.repository.ExpenseRepositoryImpl
import com.miva.billmanager.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl
    ): ExpenseRepository

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "bill_manager_db"
            ).build()
        }

        @Provides
        fun provideExpenseDao(database: AppDatabase): ExpenseDao {
            return database.expenseDao()
        }
    }
}
