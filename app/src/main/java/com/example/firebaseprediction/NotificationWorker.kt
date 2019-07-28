package com.example.firebaseprediction

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by Manish Verma on 21,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
class NotificationWorker(mContext: Context, workerParams: WorkerParameters) : Worker(mContext, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        Log.d(TAG,"Performing long running task in scheduled job")

        return ListenableWorker.Result.success()
    }
    companion object{
        private val TAG="NotificationWorker"
    }
}