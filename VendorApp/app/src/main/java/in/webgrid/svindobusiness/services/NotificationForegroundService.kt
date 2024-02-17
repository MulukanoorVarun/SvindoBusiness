    package `in`.webgrid.svindobusiness.services

    import android.annotation.SuppressLint
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.app.Service
    import android.content.Intent
    import android.media.MediaPlayer
    import android.net.Uri
    import android.os.Build
    import android.os.IBinder
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import `in`.webgrid.svindobusiness.R
    import `in`.webgrid.svindobusiness.activity.MainActivity

    class NotificationForegroundService : Service() {

        companion object {
            const val FOREGROUND_SERVICE_ID = 101
        }

        override fun onCreate() {
            super.onCreate()
            // Your initialization code, if needed
        }

        @SuppressLint("ForegroundServiceType")
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("ForegroundService", "onStartCommand Started")

            // Create an ongoing notification
            val notification = NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.svindobusiness)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name))
                .setOngoing(true)
                .build()

            startForeground(FOREGROUND_SERVICE_ID, notification)

            // Your additional code

           // createNotificationChannel()
           // showNotification("You are online")
            return START_STICKY
        }

        private fun createNotificationChannel() {

            Log.d("createNotificationChannel", "Notification channel is created")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    getString(R.string.channel_id), "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                serviceChannel.setSound(Uri.parse("android.resource://${packageName}/${R.raw.svindonotificationsound}"), null)
                val manager = getSystemService(NotificationManager::class.java)
                manager!!.createNotificationChannel(serviceChannel)
            }
        }

        private fun showNotification(input: String) {
            val notificationIntent = Intent(this, MainActivity::class.java).putExtra("flag", 1)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val CHANNEL_ID = getString(R.string.channel_id)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(input)
                .setStyle(NotificationCompat.BigTextStyle().bigText(input))
                .setSmallIcon(R.drawable.svindobusiness)
                .setGroup("Foreground Notifications")
                .setContentIntent(pendingIntent)
//                .setSound(Uri.parse("android.resource://${packageName}/${R.raw.svindonotificationsound}")) // Set custom sound here
                .build()

            val notificationSound: MediaPlayer = MediaPlayer.create(this, R.raw.svindonotificationsound)
            notificationSound.start()
            startForeground(1, notification)
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.d("ForegroundService", "onDestroy")
            // Your cleanup code, if needed
            stopForeground(true)
        }

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }
    }

