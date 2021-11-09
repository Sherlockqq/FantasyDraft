package com.midina.matches_ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class AlarmReceiver @Inject constructor(): BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context, intent: Intent) {

        var tour: Int = 0
        var homeTeam: String = ""
        var guestTeam: String = ""
        try {
             tour = intent.getIntExtra("tour", 0)
             homeTeam = intent.getStringExtra("homeTeam").toString()
             guestTeam = intent.getStringExtra("guestTeam").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        createNotificationChannel(context)
        notifyNotification(context, tour, homeTeam, guestTeam)
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Name",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(
        context: Context,
        tour: Int,
        homeTeam: String,
        guestTeam: String) {

        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Tour: $tour. Match is started!")
                .setContentText("$homeTeam vs $guestTeam")
                .setSmallIcon(R.drawable.ic_baseline_event_note_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify((0..2132343245).random(), build.build())
            Log.d("MyReceiver","notify")
        }
    }
}