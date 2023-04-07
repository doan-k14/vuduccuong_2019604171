package com.example.qltaichinhcanhan.main.ui.reminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import java.util.*

class NotificationHandler(private val context: Context) {
    fun setNotification(notificationInfo: NotificationInfo) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notificationInfo.nameNotification)
            .setContentText(notificationInfo.notificationNote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
//            .setWhen(getNotificationTime(notificationInfo))

        notificationManager.notify(1, notificationBuilder.build())

    }

    fun handleNotification(notificationInfo: NotificationInfo) {
        val intent = Intent(context, NDMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notificationInfo.nameNotification)
            .setContentText(notificationInfo.notificationNote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
//            .setWhen(getNotificationTime(notificationInfo))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
    fun getNotificationTime(notificationInfo: NotificationInfo): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
//
//        // Set notification time
//        val timeArray = notificationInfo.notificationTime!!.split(":")
//        calendar.set(Calendar.HOUR_OF_DAY, timeArray[0].toInt())
//        calendar.set(Calendar.MINUTE, timeArray[1].toInt())

        // Set notification frequency
        when(notificationInfo.notificationFrequency) {
            "Once" -> return calendar.timeInMillis
            "Daily" -> {
                if (calendar.timeInMillis < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                return calendar.timeInMillis
            }
            "Weekly" -> {
                if (calendar.timeInMillis < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 7)
                }
                return calendar.timeInMillis
            }
            else -> return calendar.timeInMillis
        }
    }

}
