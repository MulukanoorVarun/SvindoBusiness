package `in`.webgrid.svindobusiness.services
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.activity.MainActivity


@SuppressLint("Registered")
class FirebaseService : FirebaseMessagingService() {

   // var notificationId: Int = Random.nextInt()
   var i: Int = 0
    val CHANNEL_ID = "in.webgrid.svindobusiness"
    private var alarmSound: Uri? = null

    override fun onNewToken(token: String){
        super.onNewToken(token)
//        d("TAG ", "Refreshed token :: $token")
    }

    @SuppressLint("LogConditional")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage){
        super.onMessageReceived(message)
        Log.d("TAG", "onMessageReceived: ${message.data}")

        val title : String = message.notification?.title!!
        val body : String = message.notification?.body!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }
        showNotification(title,body)
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LogConditional")
    fun showNotification(title: String, body: String){

        Log.d("businesspartner", "Title: $title, Body: $body")

        //val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.svindonotificationsound)
        alarmSound = Uri.parse("android.resource://${applicationContext.packageName}/${R.raw.svindonotificationsound}")
        Log.d("Sound", "Sound URI: $alarmSound")


        try {
            val r = RingtoneManager.getRingtone(applicationContext,alarmSound)
            r.play()
        } catch (e: Exception) {
            Log.e("svindo", "showNotification: $e")
            e.printStackTrace()
        }

        var intent1  = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val launchIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent1)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }



        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSound(alarmSound)
            .setSmallIcon(R.drawable.svindobusiness)
//            .setContentTitle(title)
//            .setContentText(body)
        //    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(launchIntent)
            .setGroup("Backend Notifications")
            .setGroupSummary(true)
            .setAutoCancel(true)



        with(NotificationManagerCompat.from(this)) {
          //   notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(i++, builder.build())
            }else{
                Log.e("TAG", "Notification permission not granted.")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply{
                    description = channelDescription
                    setSound(alarmSound,
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
//            channel.setSound(alarmSound, null)
            //Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}

