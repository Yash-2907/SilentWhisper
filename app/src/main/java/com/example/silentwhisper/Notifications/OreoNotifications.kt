package com.example.silentwhisper.Notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build

class OreoNotifications(base: Context?) : ContextWrapper(base) {

    var notificationManager: NotificationManager? = null

    companion object {
        const val CHANNEL_ID = "com.example.silentwhisper"
        const val CHANNEL_NAME = "Silent Whisper"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableLights(false)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(channel)
    }

      fun getManager(): NotificationManager {
        if (notificationManager == null) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager!!
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun getOreoNotification(
        title: String?,
        body: String?,
        pendingIntent: PendingIntent?,
        soundUri: Uri?,
        icon: Int
    ): Notification.Builder {
        return Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(icon)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }
}




//package com.example.silentwhisper.Notifications
//
//import android.annotation.TargetApi
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.ContextWrapper
//import android.icu.text.CaseMap.Title
//import android.net.Uri
//import android.os.Build
//import com.google.firebase.messaging.RemoteMessage
//
//class OreoNotifications(base:Context?) : ContextWrapper(base)
//{
//     var notificationManager:NotificationManager? = null
//
//
//    companion object
//    {
//        const val CHANNEL_ID="com.example.silentwhisper"
//        const val CHANNEL_NAME="Silent Whisper"
//    }
//    init {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            createChannel()
//        }
//    }
//    @TargetApi(Build.VERSION_CODES.O)
//    fun createChannel()
//    {
//        val channel=NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
//           channel.enableLights(false)
//           channel.enableVibration(true)
//        channel.lockscreenVisibility=Notification.VISIBILITY_PRIVATE
//        getManager!!.createNotificationChannel(channel)
//
//
//    }
//    val getManager:NotificationManager? get()
//    {
//        if(notificationManager==null)
//            if(notificationManager==null)
//            {
//                notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            }
//        return notificationManager
//    }
//    @TargetApi(Build.VERSION_CODES.O)
//   fun getOreoNotification(
//       title:String?,
//       body:String?,
//       pendingIntent:PendingIntent?,
//       soundUri: Uri?,
//       icon:String?):Notification.Builder
//   {
//       return Notification.Builder(applicationContext, CHANNEL_ID)
//           .setContentIntent(pendingIntent)
//           .setContentIntent(title)
//           .setContentIntent(body)
//           .setSmallIcon(icon!!.toInt())
//           .setSound(soundUri)
//           .setAutoCancel(true)
//   }
//
//}