package com.raihan.simpleplayer.presentation.player

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING
import androidx.navigation.NavGraphBuilder
import com.raihan.navigation.Destination
import com.raihan.navigation.composable
import com.raihan.simpleplayer.R
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.presentation.splash.SplashScreen

/**
 * @author Raihan Arman
 * @date 29/10/24
 */
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(activity: PlayerActivity, playerModel: PlayerModel) {
    val context = LocalContext.current
    val adTagUri = Uri.parse(playerModel.adsUrl)

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val playWhenReady by rememberSaveable {
        mutableStateOf(true)
    }

    val enterFullscreen = { activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE }
    val exitFullscreen = {
        activity.requestedOrientation = SCREEN_ORIENTATION_USER
    }

    val imaAdsLoader = remember {
        buildImaAdsLoader(context)
    }

    val playerView = remember {
        createPlayerView(
            context = context,
            activity = activity,
        ) { isFullScreen ->
            with(context) {
                if (isFullScreen) {
                    if (activity.requestedOrientation == SCREEN_ORIENTATION_USER) {
                        enterFullscreen()
                    }
                } else {
                    exitFullscreen()
                }
            }
        }
    }

    val player = remember {
        createPlayer(
            context = context,
            imaAdsLoader = imaAdsLoader,
            playerView = playerView,
            videoUrl = playerModel.videoUrl,
            adsUri = adTagUri
        )
    }

    LaunchedEffect(player) {
        imaAdsLoader.setPlayer(player)
        player.prepare()
        player.playWhenReady = playWhenReady
    }

    BackHandler {
        if (activity.requestedOrientation == SCREEN_ORIENTATION_USER) {
            activity.finish()
        } else {
            exitFullscreen()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            imaAdsLoader.setPlayer(null)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AndroidView(
            factory = { _ ->
                playerView.also {
                    it.player = player
                }
            }, update = {
                when(lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        player.release()
                        imaAdsLoader.release()
                    }
                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )

        ContentPlayer(playerModel)
    }
}

@Composable
fun ContentPlayer(playerModel: PlayerModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            playerModel.title,
            style = TextStyle.Default.copy(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(10.dp))

        Text(
            playerModel.description,
            style = TextStyle.Default.copy(
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@OptIn(UnstableApi::class)
fun createPlayerView(
    context: Context,
    activity: PlayerActivity,
    onFullscreenClick: (Boolean) -> Unit
): PlayerView {
    val playerView = PlayerView(context)
    activity.requestedOrientation = SCREEN_ORIENTATION_USER
    playerView.controllerAutoShow = true
    playerView.keepScreenOn = true
    playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
    playerView.setFullscreenButtonClickListener(onFullscreenClick)

    return playerView
}

fun createPlayer(
    context: Context,
    imaAdsLoader: ImaAdsLoader,
    playerView: PlayerView,
    videoUrl: String,
    adsUri: Uri
) : Player {
    return ExoPlayer.Builder(context)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(context)
                .setLocalAdInsertionComponents(
                    {imaAdsLoader},
                    playerView
                )
        )
        .build()
        .apply {
            val mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .setAdsConfiguration(
                    MediaItem.AdsConfiguration.Builder(adsUri).build()
                )
                .build()
            setMediaItem(mediaItem)
        }
}

fun buildImaAdsLoader(context: Context): ImaAdsLoader {
    return ImaAdsLoader.Builder(context).build()
}