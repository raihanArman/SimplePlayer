package com.raihan.simpleplayer.presentation.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

/**
 * @author Raihan Arman
 * @date 30/10/24
 */
class PlayerActivity: ComponentActivity() {
    companion object {
        const val PLAYER_CONTENT_KEY = "PLAYER_CONTENT_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    val playerModelIntent = intent.getStringExtra(PLAYER_CONTENT_KEY)
                    playerModelIntent?.let {
                        val playerModel = it.fromJsonToPlayerModel()
                        PlayerScreen(this, playerModel)
                    }
                }
            }
        }
    }

}