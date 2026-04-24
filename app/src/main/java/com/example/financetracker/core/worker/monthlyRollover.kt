package com.example.financetracker.core.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.financetracker.core.domain.repository.TransactionRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

class MonthlyRollover(
    appContext: Context,
    params: WorkerParameters,
    private val repository : TransactionRepository
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            repository.getCurrentMonthsBudgetForRollOver(LocalDateTime.now())
            scheduleNextMonth(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun scheduleNextMonth(context: Context) {
            val now = LocalDateTime.now()
            val nextFirstOfMonth = now
                .with(TemporalAdjusters.firstDayOfNextMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
            val delay = Duration.between(now, nextFirstOfMonth).toMillis()

            val nextRequest = OneTimeWorkRequestBuilder<MonthlyRollover>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "MonthlyRolloverWork",
                ExistingWorkPolicy.REPLACE,
                request = nextRequest
            )
        }
    }
}