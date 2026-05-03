package com.example.financetracker

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import com.example.financetracker.core.worker.MonthlyRollover
import com.example.financetracker.BudgetScreen.Notification.NotificationHelper
import com.example.financetracker.core.worker.NotificationWorker
import com.example.financetracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

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
        Log.d("FinanceTrackerWork", "Scheduling MonthlyRollover")
        MonthlyRollover.scheduleNextMonth(this)

        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15 , TimeUnit.DAYS).build()
        
        Log.d("FinanceTrackerWork", "Enqueuing NotificationWorker: Will run every 3 minutes")
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "budget_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        ).also { 
            Log.d("FinanceTrackerWork", "NotificationWorker status: Enqueued. Next check in ~15 minutes.") 
        }
    }
}
