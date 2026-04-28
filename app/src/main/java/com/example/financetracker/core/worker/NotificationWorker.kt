package com.example.financetracker.core.worker

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.financetracker.core.domain.repository.TransactionRepository

class NotificationWorker(appContext: Context, workerParams: WorkerParameters , private val transactionRepository: TransactionRepository):
CoroutineWorker(appContext,workerParams)
{
    override suspend fun doWork(): Result {
        Log.d("FinanceTrackerWork", "NotificationWorker: doWork started")
        val percentage = transactionRepository.getBudgetAlertNotification()
        Log.d("FinanceTrackerWork", "NotificationWorker: percentage = ${percentage?.toInt()}")
        if (percentage != null && percentage >= 80.0) {
            Log.d("FinanceTrackerWork", "NotificationWorker: Showing notification")
            showNotification(percentage)
        }

        return Result.success()
    }
    private fun showNotification(actualPercentage: Double){
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val Notificationbuilder = NotificationCompat.Builder(applicationContext, "budget")
            .setContentTitle("Budget Alert")
            .setContentText("You have used ${actualPercentage.toInt()}% of your monthly budget limit.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        manager.notify( 1 , Notificationbuilder.build())
    }
}