package com.example.financetracker.BudgetScreen.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationHelper {

    const val BUDGET_CHANNEL_ID = "budget"
    const val BUDGET_CHANNEL_NAME = "Budgets"

    fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(
            BUDGET_CHANNEL_ID,
            BUDGET_CHANNEL_NAME,
            importance
        )

        val notificationManager =
            context.getSystemService(NotificationManager::class.java)

        notificationManager?.createNotificationChannel(channel)
    }
}