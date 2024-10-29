package com.raihan.simpleplayer.presentation.player

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
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
fun NavGraphBuilder.playerNavigation() = run {
    composable(Destination.PlayerScreen) { navBackStackEntry ->
//        val playerModelJson = navBackStackEntry.arguments?.getString(Destination.PlayerScreen.CONTENT_MODEL_KEY)
//        val playerModel = playerModelJson?.fromJsonToPlayerModel()

        PlayerScreen()

    }
}
@Composable
fun PlayerScreen() {
    val adTagUri = Uri.parse("https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast&unviewed_position_start=1&env=vp&impl=s&correlator=")
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val context = LocalContext.current

    // Track fullscreen state
    var isFullScreen by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current

    val imaAdsLoader = remember {
        ImaAdsLoader.Builder(context).build()
    }
    val playerView = remember {
        PlayerView(context)
    }

    val player = remember {
            ExoPlayer.Builder(context)
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
                        .setUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
                        .setAdsConfiguration(
                            MediaItem.AdsConfiguration.Builder(adTagUri).build()
                        )
                        .build()
                    setMediaItem(mediaItem)

                }
    }

    val playWhenReady by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(player) {
        imaAdsLoader.setPlayer(player)
        player.prepare()
        player.playWhenReady = playWhenReady
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

    Box {
        AndroidView(
            factory = { context ->
                playerView.also {
                    it.player = player
                    it.useController = true
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
    }
}