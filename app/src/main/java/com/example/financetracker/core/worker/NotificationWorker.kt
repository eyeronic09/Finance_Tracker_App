package com.example.financetracker.core.worker

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.financetracker.R
import com.example.financetracker.core.domain.repository.TransactionRepository

class NotificationWorker(appContext: Context, workerParams: WorkerParameters , private val transactionRepository: TransactionRepository):
CoroutineWorker(appContext,workerParams)
{
    override suspend fun doWork(): Result {
        Log.d("FinanceTrackerWork", "NotificationWorker: doWork started")
        val percentage = transactionRepository.getBudgetAlertNotification()
        Log.d("FinanceTrackerWork", "NotificationWorker: percentage = ${percentage?.toInt()}")
        if (percentage != null && percentage >= 100.0) {
            Log.d("FinanceTrackerWork", "NotificationWorker: Showing notification")
            showNotification(percentage)
        }

        return Result.success()
    }
    private fun showNotification(actualPercentage: Double){
        try {
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "budget")
                .setSmallIcon(R.drawable.outline_money_24)
                .setContentTitle("Budget Alert")
                .setContentText("You have used ${actualPercentage.toInt()}% of your monthly budget limit.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            
            manager.notify(1, notificationBuilder.build())
        } catch (e: Exception) {
            Log.e("FinanceTrackerWork", "NotificationWorker: Error showing notification", e)
        }
    }
}