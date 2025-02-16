package com.example.musicapp.presentation.player.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicapp.R
import com.example.musicapp.presentation.MusicApplication
import com.example.musicapp.presentation.player.player.PlayerManager
import com.example.musicapp.presentation.player.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MusicPlayerService : Service() {

    private lateinit var playerManager: PlayerManager

    private val mediaSession: MediaSessionCompat by lazy {
        MediaSessionCompat(this, "MusicPlayerService").apply { isActive = true }
    }
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        playerManager = (application as MusicApplication).component.playerManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initialNotification = buildNotification()
        startForeground(NOTIFICATION_ID, initialNotification)

        val action = PlayerAction.fromString(intent?.action)
        action?.let {
            when (it) {
                is PlayerAction.Play -> {
                    playerManager.play()
                }

                is PlayerAction.Pause -> {
                    playerManager.pause()
                }

                is PlayerAction.Next -> {
                    playerManager.nextTrack()
                }

                is PlayerAction.Previous -> {
                    playerManager.previousTrack()
                }

                is PlayerAction.StartService -> {
                    playerManager.play()
                }
            }
        }

        updateNotification()
        return START_STICKY
    }

    private fun buildNotification(): Notification {
        val playPauseIcon = if (playerManager.playerState.value == PlayerState.Playing) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }

        val playPauseAction = if (playerManager.playerState.value == PlayerState.Playing) {
            PlayerAction.Pause.action
        } else {
            PlayerAction.Play.action
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle(getString(R.string.now_playing))
            .setContentText(getString(R.string.notification_content_text))
            .setLargeIcon(null as Bitmap?)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_skip_previous,
                    getString(R.string.notification_previous),
                    getServicePendingIntent(PlayerAction.Previous.action)
                )
            )
            .addAction(
                NotificationCompat.Action(
                    playPauseIcon,
                    if (playerManager.playerState.value == PlayerState.Playing)
                        getString(R.string.notification_pause)
                    else
                        getString(R.string.notification_play),
                    getServicePendingIntent(playPauseAction)
                )
            )
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_skip_next,
                    getString(R.string.notification_next),
                    getServicePendingIntent(PlayerAction.Next.action)
                )
            )
            .build()
    }

    private fun getServicePendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun updateNotification() {
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
            }
        } else {
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
        mediaSession.release()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "music_player_channel"
        const val NOTIFICATION_ID = 1
    }
}





