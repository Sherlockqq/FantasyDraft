package com.midina.team_ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.midina.team_ui.ClubFragment.Companion.AWAY
import com.midina.team_ui.ClubFragment.Companion.HOME
import com.midina.team_ui.ClubFragment.Companion.TOUR
import javax.inject.Inject

class AlarmReceiver @Inject constructor() : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context, intent: Intent) {

        var tour = ""
        var homeTeam = ""
        var awayTeam = ""

        try {
            tour = intent.getStringExtra(TOUR).toString()
            homeTeam = intent.getStringExtra(HOME).toString()
            awayTeam = intent.getStringExtra(AWAY).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        createNotificationChannel(context)
        notifyNotification(context, tour, homeTeam, awayTeam)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "MatchNotificationChannel",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(
        context: Context,
        tour: String,
        homeTeam: String,
        awayTeam: String
    ) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("$tour. Match is started!")
                .setContentText("$homeTeam vs $awayTeam")
                .setSmallIcon(R.drawable.ic_baseline_event_note_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify((0..2132343245).random(), build.build())
            Log.d("MyReceiver", "notify")
        }
    }
}