//package `in`.webgrid.svindobusiness
//import android.annotation.SuppressLint
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.ContentResolver
//import android.content.Context
//import android.content.Intent
//import android.media.AudioAttributes
//import android.media.RingtoneManager
//import android.net.Uri
//import android.os.Build
//import android.util.Log
//import android.widget.RemoteViews
//import androidx.core.app.NotificationCompat
//import androidx.test.core.app.ApplicationProvider
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import `in`.webgrid.svindobusiness.activity.MainActivity
//
//
//@SuppressLint("Registered")
//class MyFirebaseMessagingService  : FirebaseMessagingService() {
//    val channel_id = "notification_channel"
//    val channel_name = "svindobusiness"
//
//    @SuppressLint("LogConditional")
//    override fun onNewToken(token: String){
//        super.onNewToken(token)
//        Log.d("MyFirebaseMessagingService", "Refreshed token: $token")
//    }
//    // Override onMessageReceived() method to extract the
//    // title
//    // and
//    // body from the message passed in FCM
//    @SuppressLint("LogConditional")
//    override fun onMessageReceived(remoteMessage: RemoteMessage){
//        // First case when notifications are received via
//        // data event
//        // Here, 'title' and 'message' are the assumed names
//        // of JSON
//        // attributes. Since here we do not have any data
//        // payload, This section is commented out. It is
//        // here only for reference purposes.
//        /*if(remoteMessage.getData().size()>0){
//            showNotification(remoteMessage.getData().get("title"),
//                          remoteMessage.getData().get("message"));
//        }*/
//        // Second case when notification payload is
//        // received.
//        Log.i("SellerFirebaseService ", "Message :: $remoteMessage")
//
//        if(remoteMessage.getNotification()!= null){
//            // Since the notification is received directly from
//            // FCM, the title and the body can be fetched
//            // directly as below.
//            showNotification(remoteMessage.getNotification()!!.getTitle()!! , remoteMessage.getNotification()!!.getBody()!!)
//        }
//    }
//
//    private fun getCustomDesign(title: String, message: String): RemoteViews {
//        val remoteViews = RemoteViews(ApplicationProvider.getApplicationContext<Context>().getPackageName(), R.layout.activity_notifications_showing)
//        remoteViews.setTextViewText(R.id.title, title)
//        remoteViews.setTextViewText(R.id.message, message)
//        remoteViews.setImageViewResource(R.id.icon, R.drawable.svindobusiness)
//        return remoteViews
//    }
//
//
//    @SuppressLint("UnspecifiedImmutableFlag", "LogConditional")
//    fun showNotification(title: String, message: String) {
//
//        Log.d("TAG", "Title: $title, Body: $message")
//
//        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.foodsound)
//        try {
//            val r = RingtoneManager.getRingtone(applicationContext,alarmSound)
//            r.play()
//        } catch (e: Exception) {
////            d("TAG", "showNotification: $e")
//            e.printStackTrace()
//        }
//
//        // Pass the intent to switch to the MainActivity
//
//        val intent = Intent(this, MainActivity::class.java)
//        // Assign channel ID
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//   //     val notification = Notification()
//        var builder: NotificationCompat.Builder = NotificationCompat.Builder(ApplicationProvider.getApplicationContext<Context>(),channel_id)
//            .setSmallIcon(R.drawable.svindobusiness)
//            .setAutoCancel(true)
//            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//            .setSound(alarmSound)
//      //  notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.kalimba)
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        builder = builder.setContent(getCustomDesign(title, message))
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(channel_id,channel_name, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager!!.createNotificationChannel(notificationChannel)
//        }
//        notificationManager!!.notify(0, builder.build())
//    }
//
//}