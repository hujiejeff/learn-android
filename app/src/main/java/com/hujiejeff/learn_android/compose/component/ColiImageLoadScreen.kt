package com.hujiejeff.learn_android.compose.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import coil.imageLoader


@Composable
fun ColiImageLoadScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        WithLoadingImage("")
        ThreeCircleLoading()
    }
}

@Composable
fun WithLoadingImage(url: String) {
    SubcomposeAsyncImage(
        model = "https://images.unsplash.com/photo-1703511606233-9c7537658701?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        loading = { state ->
            CircularProgressIndicator()
        },
        contentDescription = null
    )
}

@Composable
fun NewWorkImage(
    modifier: Modifier = Modifier,
    data: Any,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val imagePainter = rememberImagePainter(
        data = data,
        imageLoader = LocalContext.current.imageLoader,
        builder = {
        })
    Image(
        modifier = modifier,
        painter = imagePainter,
        contentDescription = contentDescription,
        contentScale = contentScale
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeCircleLoading() {
    val infiniteTransition = rememberInfiniteTransition()
    val scaleValueAnimations = (0 until 3).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(200 * index)
            )
        )
    }

    val color = MaterialTheme.colorScheme.onSurface
    val radius = with(LocalDensity.current) { 10.dp.toPx() }
    val padding = with(LocalDensity.current) { 5.dp.toPx() }
    Canvas(modifier = Modifier.padding(4.dp), contentDescription = "") {
        var x = 0f
        val y = radius
        repeat(3) { index ->
            x = (2 * index + 1) * radius + padding * index
            scale(scaleValueAnimations[index].value, pivot = Offset(x, y)) {
                drawCircle(color = color, radius = radius, center = Offset(x, y))
            }
        }
    }
}