package com.midina.matches_ui

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import java.lang.Thread.sleep
import android.media.RingtoneManager
import android.net.Uri


class MatchWorker (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
        const val KEY_LIST = "LIST_KEY"
    }

    override fun doWork(): Result {

        var counter = 0
        while(counter < 5){
            counter++
            Log.d("MatchWorker", "Counter = $counter")
        }
        return if(counter == 5) {
            showNotification()
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun showNotification(){

        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val title = (0..2132343245).random().toString()
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_menu_send)
            .setContentTitle(title)
            .setContentText("Subscribe on the channel")
            .setVisibility(VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(alarmSound)
            .setVibrate(pattern)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(title.toInt(), notification.build())
        }
    }

}