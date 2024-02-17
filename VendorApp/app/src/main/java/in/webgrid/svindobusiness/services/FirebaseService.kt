package `in`.webgrid.svindobusiness.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.activity.MainActivity

@SuppressLint("Registered")
class FirebaseService : FirebaseMessagingService() {

    private var i: Int = 0

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @SuppressLint("LogConditional")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("TAG", "onMessageReceived: ${message.data}")

        val title: String = message.notification?.title ?: ""
        val body: String = message.notification?.body ?: ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        showNotification(title, body)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, NotificationForegroundService::class.java))
//        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LogConditional")
    fun showNotification(title: String, body: String) {
        Log.d("businesspartner", "Title: $title, Body: $body")

        val intent1 = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val launchIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

        val CHANNEL_ID = getString(R.string.channel_id)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.svindobusiness)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setGroup("Backend Notifications")
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(launchIntent)

        val notificationSound: MediaPlayer = MediaPlayer.create(this, R.raw.svindonotificationsound)
        notificationSound.start()

        val notificationManager = getSystemService(NotificationManager::class.java)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, NotificationForegroundService::class.java))
//        }

        notificationManager.notify(i++, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channelName = getString(R.string.channel_name)
        val channelDescription = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(getString(R.string.channel_id), channelName, importance)
        channel.description = channelDescription
        channel.setSound(
            Uri.parse("android.resource://${applicationContext.packageName}/${R.raw.svindonotificationsound}"),
            audioAttributes
        )

        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
