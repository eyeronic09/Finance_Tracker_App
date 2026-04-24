package com.example.financetracker.di

import androidx.room.Room
import com.example.financetracker.AddTransaction.AddTransactionVM
import com.example.financetracker.BudgetScreen.UI_Screen.BudgetChartVM
import com.example.financetracker.BudgetScreen.UI_Screen.BudgetViewModel
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenViewModel
import com.example.financetracker.core.data.local.database.AppDatabase
import com.example.financetracker.core.data.repository.TransactionRepositoryImpl
import com.example.financetracker.core.domain.repository.TransactionRepository
import com.example.financetracker.core.worker.MonthlyRollover
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "finance_tracker_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // DAOs
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().budgetDao() }

    // Repository
    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            categoryDao = get(),
            budgetDao = get()
        )
    }

    // ViewModels
    viewModel { HomeScreenViewModel(repository = get()) }
    viewModel { BudgetViewModel(repository = get()) }
    viewModel { AddTransactionVM(categoryRepository = get()) }
    viewModel { BudgetChartVM(repository = get()) }

    // Worker
    worker { MonthlyRollover(appContext = get(), params = get(), repository = get()) }
}
