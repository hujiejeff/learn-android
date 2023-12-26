package com.hujiejeff.learn_android.compose.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ProgressIndicator(refreshing: Boolean, progress: Float) {
    /* CircularProgressIndicator(
         modifier = Modifier
             .size(if (refreshing) 50.dp else 30.dp * progress)
             .rotate(progress * 360),
         strokeWidth = if (refreshing) 5.dp else 3.dp
     )*/
    val p: Float = if (!refreshing) {
        progress
    } else {
        val infiniteTransition = rememberInfiniteTransition()
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    500,
                    easing = LinearEasing
                ), repeatMode = RepeatMode.Restart
            )
        ).value
    }
    Box(
        modifier = Modifier
            .offset(0.dp, ((progress) * 50).dp)
            .rotate(p * 360)
    ) {
        Icon(imageVector = Icons.Default.Downloading, contentDescription = null)
    }
}


@Composable
fun PullToRefresh(modifier: Modifier = Modifier) {
    var refreshing by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) } // progress 代表拖动的进度
    var offsetY by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Content
        Column(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            //你的内容写在这
            repeat(100) {
                Text(text = "test")
            }
        }

        // Progress Indicator
        Box(Modifier.align(Alignment.TopCenter)) {
            ProgressIndicator(refreshing, progress)
        }

        // 可拖动区域
        val draggableState = rememberDraggableState { delta ->
            if (offsetY + delta <= 500f) {
                offsetY += delta
                progress = offsetY / 500f
            }

            if (offsetY + delta <= 0) {
                offsetY = 0f
                progress = 0f
            }
//            refreshing = offsetY > 250f
        }

        Box(
            Modifier
                .fillMaxSize()
                .draggable(
                    orientation = Orientation.Vertical,
                    state = draggableState,
                    onDragStopped = {
                        // Drag 结束
                        refreshing = true
                        offsetY = 0f
                        progress = 0f
                        scope.launch {
                            delay(2000)
                            refreshing = false
                        }
                    }
                )
        )
    }
}

@Composable
fun SwipeRefreshDemo() {
    Box(modifier = Modifier.fillMaxSize()) {
        // 是否正在刷新
        var isRefreshing by remember { mutableStateOf(false) }
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val swipeState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
        val density = LocalDensity.current
        val dpValue =
        SwipeRefresh(
            state = swipeState,
            onRefresh = {
                scope.launch {
                    // 这里执行刷新操作，比如获取网络数据
                    isRefreshing = true
                    delay(2000) // 模拟耗时操作
                    // 刷新结束
                    isRefreshing = false
                }
            }
        ) {
            // 列表内容
            Text(text = "底层内容")
            Surface(modifier = Modifier.offset(0.dp, with(density) { swipeState.indicatorOffset.toDp() })) {
                LazyColumn(state = listState) {
                    items(20) { index ->
                        Text(
                            "Item $index",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}