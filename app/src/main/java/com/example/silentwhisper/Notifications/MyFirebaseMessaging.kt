package com.example.silentwhisper.Notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.silentwhisper.ChatPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(mRemoteMessage: RemoteMessage) {
        super.onMessageReceived(mRemoteMessage)

        val sent = mRemoteMessage.data["sent"]
        val user = mRemoteMessage.data["user"]
        val sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)

        val currOnlineUser = sharedPref.getString("currentUser", "none")
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null && sent == firebaseUser.uid) {
            if (currOnlineUser == null || currOnlineUser != user) {
                // Notify only if the current online user is different
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(mRemoteMessage)
                } else {
                    sendNotification(mRemoteMessage)
                }
            }
        }
    }

     fun sendNotification(mRemoteMessage: RemoteMessage) {
        val user = mRemoteMessage.data["user"]
        val icon = mRemoteMessage.data["icon"]
        val title = mRemoteMessage.data["title"]
        val body = mRemoteMessage.data["body"]

        if (user == null || icon == null || title == null || body == null) {
            // Prevent null values causing crashes
            return
        }

        val j = user.replace("[\\D]".toRegex(), "").toIntOrNull() ?: 0
        val intent = Intent(this, ChatPage::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, j, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, "default_channel_id")
            .setSmallIcon(icon.toIntOrNull() ?: android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(j, builder.build())
    }

     fun sendOreoNotification(mRemoteMessage: RemoteMessage) {
        val user = mRemoteMessage.data["user"]
        val icon = mRemoteMessage.data["icon"]
        val title = mRemoteMessage.data["title"]
        val body = mRemoteMessage.data["body"]

        if (user == null || icon == null || title == null || body == null) {
            // Prevent null values causing crashes
            return
        }

        val j = user.replace("[\\D]".toRegex(), "").toIntOrNull() ?: 0
        val intent = Intent(this, ChatPage::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, j, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreoNotification = OreoNotifications(this)
        val builder: Notification.Builder = oreoNotification.getOreoNotification(
            title, body, pendingIntent, defaultSound, icon
        )

        oreoNotification.getManager()?.notify(j, builder.build())
    }
}






//package com.example.silentwhisper.Notifications
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Build
//import android.os.Bundle
//import androidx.core.app.NotificationCompat
//import com.example.silentwhisper.ChatPage
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//class MyFirebaseMessaging : FirebaseMessagingService()
//{
//    override fun onMessageReceived(mRemoteMessage: RemoteMessage)
//    {
//        super.onMessageReceived(mRemoteMessage)
//        val sent = mRemoteMessage.data["sent"]
//        val user=mRemoteMessage.data["user"]
//        val sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
//
//        val currOnlineUser=sharedPref.getString("currentUser","none")
//
//        val firebaseUser=FirebaseAuth.getInstance().currentUser
//        if(firebaseUser!=null && sent==firebaseUser.uid)
//        {
//            if(currOnlineUser!=null)
//            {
//               return true
//            } else {
//                return false
//            }
//
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                {
//                    sendOreoNotification(mRemoteMessage)
//
//                }else
//                {
//                    sendNotification(mRemoteMessage)
//
//                }
//
//        }
//     }
//    fun sendNotification(mRemoteMessage: RemoteMessage)
//    {
//        val user = mRemoteMessage.data["user"]
//        val icon = mRemoteMessage.data["icon"]
//        val title = mRemoteMessage.data["title"]
//        val body = mRemoteMessage.data["body"]
//
//        val notification = mRemoteMessage.notification
//        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
//        val intent =Intent(this,ChatPage::class.java)
//        val bundle=Bundle()
//        bundle.putString("userId",user)
//        intent.putExtras(bundle)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent= PendingIntent.getActivity(this,j,intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE )
//        val defaultSound =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder:NotificationCompat.Builder= NotificationCompat.Builder(this)
//            .setSmallIcon(icon!!.toInt())
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setSound(defaultSound)
//            .setContentIntent(pendingIntent)
//
//        val noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//
//
//        var i=0
//        if(j>0)
//        {
//            i=j
//        }
//        noti.notify(i,builder.build())
//    }
//    fun sendOreoNotification(mRemoteMessage: RemoteMessage)
//    {
//        val user = mRemoteMessage.data["user"]
//        val icon = mRemoteMessage.data["icon"]
//        val title = mRemoteMessage.data["title"]
//        val body = mRemoteMessage.data["body"]
//
//        val notification = mRemoteMessage.notification
//        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
//        val intent =Intent(this,ChatPage::class.java)
//        val bundle=Bundle()
//        bundle.putString("userId",user)
//        intent.putExtras(bundle)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent= PendingIntent.getActivity(this,j,intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE )
//        val defaultSound =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val oreoNotification= OreoNotifications(this)
//        val builder:Notification.Builder=oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon)
//        var i=0
//        if(j>0)
//        {
//            i=j
//        }
//        oreoNotification.getManager!!.notify(i,builder.build())
//    }
//
//
//
//
//}
