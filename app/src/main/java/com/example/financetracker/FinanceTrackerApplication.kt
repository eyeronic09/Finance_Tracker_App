package com.example.financetracker

import android.app.Application
import com.example.financetracker.core.worker.MonthlyRollover
import com.example.financetracker.core.worker.NotificationHelper
import com.example.financetracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class FinanceTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FinanceTrackerApplication)
            workManagerFactory()
            modules(appModule)
        }
        
        NotificationHelper.createNotificationChannel(this)
        MonthlyRollover.scheduleNextMonth(this)
    }
}
