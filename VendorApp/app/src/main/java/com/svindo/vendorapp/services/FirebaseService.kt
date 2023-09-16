package com.svindo.vendorapp.services
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.svindo.vendorapp.R
import com.svindo.vendorapp.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


@SuppressLint("Registered")
class FirebaseService : FirebaseMessagingService() {
    // var i: Int = 0
    var notificationId: Int = Random.nextInt()
    override fun onNewToken(token: String){
        super.onNewToken(token)
//        d("TAG ", "Refreshed token :: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//        d("TAG", "onMessageReceived: ${message.data}")


        showNotification(message.data)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(
        data: MutableMap<String, String>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val type = data["type"]
        val title = data["title"]
        val body = data["message"]
        val alarmSound = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.foodsound
        )
        try {
            val r = RingtoneManager.getRingtone(applicationContext,alarmSound)
            r.play()
        } catch (e: Exception) {
//            d("TAG", "showNotification: $e")
            e.printStackTrace()
        }

        var intent1  = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val launchIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent1)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

        }

        val acceptIntent = Intent(this,MainActivity::class.java)
            .apply {
                action = "accept"
                putExtra("UserID","100")
                putExtra("Name","Jones")
                putExtra("notificationId", notificationId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val acceptPendingIntent = PendingIntent.getActivity(this, 0, acceptIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val rejectIntent = Intent(baseContext, MainActivity::class.java)
            .apply {

                action = "reject"
                putExtra("UserID","100")
                putExtra("notificationId", notificationId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val rejectPendingIntent = PendingIntent.getBroadcast(baseContext, 0, rejectIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.svindobusiness)
            .setContentTitle(type)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(launchIntent)
            .setGroup("Backend Notifications")
            .setVibrate(longArrayOf( 1000, 1000, 1000, 1000, 1000 ))
            .setLights(Color.RED, 3000, 3000)
            .setGroupSummary(true)
            .setAutoCancel(true)
        // Set the intent that will fire when the user taps the notification
        //  .addAction(android.R.drawable.ic_menu_view, "ACCEPT", acceptPendingIntent)
        //   .addAction(android.R.drawable.ic_delete, "REJECT", rejectPendingIntent)

        // .setColor(ContextCompat.getColor(this, R.color.orange))

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
//            notify(notificationId, builder.build())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(getString(R.string.channel_id), name, importance)
            .apply {
                description = descriptionText
                lightColor = R.color.orange
                enableLights(true)
            }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", channelName, importance).apply {
                description = channelDescription
            }

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun getBitmapFromURL(strURL: String): Bitmap? {
        var myBitmap: Bitmap? = null
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            myBitmap = BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return myBitmap
    }

}