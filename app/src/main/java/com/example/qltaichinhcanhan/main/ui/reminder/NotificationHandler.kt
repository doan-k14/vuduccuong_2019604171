package com.example.qltaichinhcanhan.main.ui.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.splash.OnBoardingScreenActivity
import java.util.*

class NotificationHandler(private val context: Context) {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "default_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Default Channel"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION =
            "This is the default channel for notifications."
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            )
            channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun showNotification(notification: NotificationInfo) {
        val intent = Intent(context, OnBoardingScreenActivity::class.java)
        intent.putExtra("notification", notification)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_noti)
            .setContentTitle(notification.nameNotification)
            .setContentText(notification.notificationNote)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notification.idNotification, builder.build())
    }

    fun cancelNotification(notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    fun scheduleNotification(notification: NotificationInfo) {
        val notificationTime = notification.notificationTime ?: return
        val notificationReminderStartTime = notification.notificationReminderStartTime ?: return
        val notificationFrequency = notification.notificationFrequency ?: return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, OnBoardingScreenActivity::class.java)
        intent.putExtra("notification", notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.idNotification,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        when (notificationFrequency) {
            "Once" -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    pendingIntent
                )
            }
            "Daily" -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    notificationReminderStartTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            "Weekly" -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    notificationReminderStartTime,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
            "Monthly" -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = notificationTime
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    notificationReminderStartTime,
                    AlarmManager.INTERVAL_DAY * 30,
                    pendingIntent
                )

                // Check if the notification should be shown on the last day of the month
                if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    val lastDayOfMonth = calendar.clone() as Calendar
                    lastDayOfMonth.add(Calendar.MONTH, 1)
                    lastDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
                    lastDayOfMonth.add(Calendar.DAY_OF_MONTH, -1)

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        lastDayOfMonth.timeInMillis,
                        pendingIntent
                    )
                }
            }
            "Quarterly" -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    notificationReminderStartTime,
                    AlarmManager.INTERVAL_DAY * 90,
                    pendingIntent
                )
            }
            "Yearly" -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    pendingIntent
                )
            }
        }
    }

    fun cancelScheduledNotification(notificationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, OnBoardingScreenActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val notification = intent.getParcelableExtra<NotificationInfo>("notification")
                if (notification != null) {
                    val notificationHandler = NotificationHandler(context!!)
                    notificationHandler.showNotification(notification)
                    // Set up next notification based on the frequency
                    val frequency = notification.notificationFrequency
                    val reminderStartTime = notification.notificationReminderStartTime ?: 0
                    val nextNotificationTime = getNextNotificationTime(frequency, reminderStartTime)
                    if (nextNotificationTime != null) {
                        val alarmManager =
                            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val pendingIntent = createNotificationIntent(context, notification)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                nextNotificationTime,
                                pendingIntent
                            )
                        }
                    }
                }
            }
        }

        private fun createNotificationIntent(
            context: Context,
            notification: NotificationInfo,
        ): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("notification", notification)
            return PendingIntent.getBroadcast(
                context,
                notification.idNotification,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        private fun getNextNotificationTime(frequency: String?, reminderStartTime: Long): Long? {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = reminderStartTime
            when (frequency) {
                "Daily" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                "Weekly" -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
                "Monthly" -> calendar.add(Calendar.MONTH, 1)
                "Quarterly" -> calendar.add(Calendar.MONTH, 3)
                "Yearly" -> calendar.add(Calendar.YEAR, 1)
                else -> return null
            }
            return calendar.timeInMillis
        }

//        Để lên lịch thông báo đầu tiên
        fun scheduleNotification(context: Context, notification: NotificationInfo) {
            val reminderStartTime = notification.notificationReminderStartTime ?: 0
            val nextNotificationTime =
                getNextNotificationTime(notification.notificationFrequency, reminderStartTime)
            if (nextNotificationTime != null) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntent = createNotificationIntent(context, notification)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextNotificationTime,
                        pendingIntent
                    )
                }

            }
        }
    }
}
