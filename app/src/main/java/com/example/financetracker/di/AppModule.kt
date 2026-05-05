package com.example.financetracker.di

import androidx.room.Room
import com.example.financetracker.AddTransaction.AddTransactionVM
import com.example.financetracker.BudgetScreen.UI_Screen.BudgetChartVM
import com.example.financetracker.BudgetScreen.UI_Screen.BudgetViewModel
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenViewModel
import com.example.financetracker.SettingScreen.Ui.Screen.ViewModel.CategoryManagementVM
import com.example.financetracker.SettingScreen.Ui.Screen.ViewModel.SettingVM
import com.example.financetracker.SettingScreen.domain.repository.SettingPrefReposistory
import com.example.financetracker.core.data.local.database.AppDatabase
import com.example.financetracker.core.data.repository.GoalRepositoryImpl
import com.example.financetracker.core.data.repository.TransactionRepositoryImpl
import com.example.financetracker.core.domain.repository.GoalRepository
import com.example.financetracker.core.domain.repository.TransactionRepository
import com.example.financetracker.core.worker.MonthlyRollover
import com.example.financetracker.core.worker.NotificationWorker
import com.example.financetracker.goals.GoalViewModel
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
    single { get<AppDatabase>().goalDao() }

    // Repository
    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            categoryDao = get(),
            budgetDao = get()
        )
    }

    single<GoalRepository> {
        GoalRepositoryImpl(goalDao = get())
    }

    single { SettingPrefReposistory(context = androidApplication()) }

    // ViewModels
    viewModel { HomeScreenViewModel(repository = get()) }
    viewModel { BudgetViewModel(repository = get()) }
    viewModel { (initialCategory: String?) -> AddTransactionVM(categoryRepository = get(), initialCategory = initialCategory) }
    viewModel { BudgetChartVM(repository = get()) }
    viewModel { SettingVM(settingPrefReposistory = get()) }
    viewModel { CategoryManagementVM(repository = get()) }
    viewModel { GoalViewModel(repository = get()) }

    // Worker
    worker { MonthlyRollover(appContext = get(), params = get(), repository = get()) }
    worker { NotificationWorker(appContext = get(), workerParams = get(), transactionRepository = get()) }
}
