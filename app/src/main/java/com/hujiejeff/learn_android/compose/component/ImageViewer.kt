package com.hujiejeff.learn_android.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.hujiejeff.learn_android.R

@Composable
fun ImageViewerDemoScreen(modifier: Modifier = Modifier) {
    var isShow by remember { mutableStateOf(false) }
    var smallRect by remember { mutableStateOf<Rect?>(null) }
    Box(modifier = modifier.fillMaxSize()) {
        Column() {
            Thumbnail() {
                smallRect = it
                isShow = true
            }
        }

        if (isShow) {
            Dialog(onDismissRequest = { /*TODO*/ }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    BigImage() {
                        isShow = false
                    }
                }
            }
        }
    }
}

@Composable
fun Thumbnail(onClick: (Rect) -> Unit) {
    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    Image(
        painter = painterResource(id = R.drawable.ab2_quick_yoga),
        contentDescription = null,
        modifier = Modifier
            .size(200.dp)
            .onGloballyPositioned {
                layoutCoordinates = it
            }
            .clickable {
                layoutCoordinates?.let { lc ->
                    val globalPosition = lc.positionInWindow()
                    val size = lc.size.toSize()
                    onClick(Rect(globalPosition.x, globalPosition.y, size.width, size.height))
                }
            },
        contentScale = ContentScale.Crop
    )
}


@Composable
fun Test2(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .size(50.dp)
            .wrapContentWidth(unbounded = true)
    ) {
        SmallImage() {

        }
    }
}

@Composable
fun Test1(modifier: Modifier = Modifier) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = modifier.height(200.dp))
            SmallImage() {
                isShow = true
            }
        }

        AnimatedContent(targetState = isShow, transitionSpec = {
            scaleIn(animationSpec = tween(2000)) togetherWith scaleOut(animationSpec = tween(2000))
        }) {
            if (it) {
                BigImage() {
                    isShow = false
                }
            }
        }
    }
}

@Composable
fun SmallImage(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Image(
        modifier = modifier
            .size(100.dp)
            .zIndex(2f)
            .clickable(onClick = onClick),
        painter = painterResource(id = R.drawable.ab2_quick_yoga),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun BigImage(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        painter = painterResource(id = R.drawable.ab2_quick_yoga),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}