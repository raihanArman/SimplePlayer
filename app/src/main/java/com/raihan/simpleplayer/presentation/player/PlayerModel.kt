package com.raihan.simpleplayer.presentation.player

import com.google.gson.Gson
import com.raihan.simpleplayer.domain.ContentModel

/**
 * @author Raihan Arman
 * @date 29/10/24
 */
data class PlayerModel(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String,
    val thumbUrl: String,
)

fun ContentModel.mapToPlayerModel(): PlayerModel {
    return PlayerModel(
        id = this.id,
        title = this.title,
        description = this.description,
        videoUrl = this.videoUrl,
        adsUrl = this.adsUrl,
        thumbUrl = this.thumbUrl
    )
}

// Convert PlayerModel to JSON
fun PlayerModel.toJson(): String = Gson().toJson(this)

// Convert JSON to PlayerModel
fun String.fromJsonToPlayerModel(): PlayerModel = Gson().fromJson(this, PlayerModel::class.java)