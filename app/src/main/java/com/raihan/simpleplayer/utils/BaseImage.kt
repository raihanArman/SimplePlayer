package com.raihan.simpleplayer.utils

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import com.raihan.simpleplayer.R

/**
 * @author Raihan Arman
 * @date 29/10/24
 */

@Composable
fun BaseImageView(
    url: String,
    modifier: Modifier,
    contentScale: ContentScale? = null,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(LoggerInterceptor())
        }
        .build()

    SubcomposeAsyncImage(
        model = url,
        modifier = modifier,
        loading = {
            DefaultLoadingView()
        },
        error = {
            Image(painter = painterResource(id = R.drawable.ic_placeholder), contentDescription = null)
        },
        success = {
            val state = painter.state
            if (state is AsyncImagePainter.State.Success) {
                Image(
                    painter = this.painter, contentDescription = "",
                    modifier = modifier,
                    contentScale = contentScale ?: ContentScale.Fit,
                )
            }
        },

        contentDescription = "",
        imageLoader = imageLoader
    )
}

@Composable
fun DefaultLoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize(),
        )
    }
}

class LoggerInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val result = chain.proceed(chain.request)
        if (result is ErrorResult) {
            println("Image load error: ${result.throwable.message}")
        }
        return result
    }
}