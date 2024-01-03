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
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
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
    /*var isShow by remember { mutableStateOf(false) }
    var smallRect by remember { mutableStateOf<Rect?>(null) }
    Box(modifier = modifier.fillMaxSize()) {
        Column() {
            Thumbnail() {
                smallRect = it
                isShow = true
            }
        }

        if (isShow) {
            Dialog(onDismissRequest = { *//*TODO*//* }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    BigImage() {
                        isShow = false
                    }
                }
            }
        }
    }*/
    var draggableOffset by remember {
        mutableStateOf(0f)
    }
    var consumedOffset by remember {
        mutableStateOf(0f)
    }
    var availabeOffset by remember {
        mutableStateOf(0f)
    }
    val scrollState = rememberScrollState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                /*if (available == Offset.Zero) {
                    // 子Compose已经滑动到了极限状态，无法再滚动了
                    draggableOffset = consumed.y
                }*/
                consumedOffset = consumed.y
                availabeOffset = available.y
                return super.onPostScroll(consumed, available, source)
            }
            //...
        }
    }
    val draggableState = remember {
        DraggableState { delta ->
            draggableOffset += delta
        }
    }
    /*Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .draggable(
                state = draggableState,
                orientation = Orientation.Vertical,
                onDragStarted = { *//* 在拖动开始时，你可以在这里做一些事情 *//* },
                onDragStopped = { *//* 在拖动停止时，你可以在这里做一些事情 *//* }
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(1300.dp),
            painter = painterResource(id = R.drawable.long_pic),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column() {
            Text(text = "consumedOffset: ${consumedOffset}", color = Color.White)
            Text(text = "availabeOffset: ${availabeOffset}", color = Color.White)
            Text(text = "draggableOffset: ${draggableOffset}", color = Color.White)
        }
    }*/
    TransformableSample()
}

@Composable
fun TransformableSample() {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    Box(
        Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ab2_quick_yoga),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Text(text = "${state.isTransformInProgress}")
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