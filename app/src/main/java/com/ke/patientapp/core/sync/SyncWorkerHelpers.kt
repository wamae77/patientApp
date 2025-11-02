package com.ke.patientapp.core.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import com.ke.patientapp.R

val netConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()

val backoff = BackoffPolicy.EXPONENTIAL


private const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "SyncNotificationChannel"


fun Context.syncForegroundInfo(
    channelName: String,
    title: String,
    contentText: String? = null,
    notDescription: String,
    progress: Int? = null
) = ForegroundInfo(
    SYNC_NOTIFICATION_ID,
    syncWorkNotification(
        channelName,
        title,
        notDescription,
        contentText, progress = progress
    ),
)


private fun Context.syncWorkNotification(
    channelName: String,
    title: String,
    notDescription: String,
    contentText: String? = null,
    iconRes: Int = R.drawable.ic_launcher_foreground,
    progress: Int? = null
): Notification {
    val channel = NotificationChannel(
        SYNC_NOTIFICATION_CHANNEL_ID,
        channelName,
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = notDescription
    }

    val notificationManager: NotificationManager? =
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    notificationManager?.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(this, SYNC_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(iconRes)
        .setContentTitle(title)
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    //.setOngoing(true)

    progress?.let {
        builder.setProgress(100, it, false)
    }

    return builder.build()
}




