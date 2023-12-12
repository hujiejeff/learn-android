package com.hujiejeff.learn_android.compose.codelab_animation

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationPracticeScreen(modifier: Modifier = Modifier) {
    var isShowCardA by remember {
        mutableStateOf(false)
    }
    var selectA by remember {
        mutableStateOf(true)
    }

    Column(
        modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        //AnimatedVisibility
        AnimatedVisibility(
            visible = isShowCardA,
            enter = slideIn(
                animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
            ) {
                IntOffset(0, -it.height)
            },
            exit = fadeOut(animationSpec = tween(delayMillis = 1500, durationMillis = 3000))
        ) {
            val background by transition.animateColor(label = "1") { state ->
                if (state == EnterExitState.Visible) Color.Blue else Color.Gray
            }
            val sizePercent by transition.animateFloat(label = "size", transitionSpec = {
                tween(3000)
            }) { state ->
                if (state == EnterExitState.Visible) 1f else 0f
            }

            //自定义shape需要remember一下
            val shape = remember(sizePercent) {
                GenericShape { size, _ ->
                    addOval(Rect(Offset(size.width / 2, size.height / 2), size.width * sizePercent))
                }
            }
            val cornerDp by transition.animateDp(label = "2", transitionSpec = {
                tween(3000)
            }) { state ->
                if (state == EnterExitState.Visible) 0.dp else 20.dp
            }
            CardA(
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape)
                    .background(background)

            )
        }
        Text(text = "AnimatedVisibility动画练习")
        Button(onClick = {
            isShowCardA = !isShowCardA
        }) {
            Text(text = if (isShowCardA) "hide" else "show")
        }


        //AnimatedContent切换
        AnimatedContent(
            targetState = selectA,
            transitionSpec = {
//                fadeIn(tween(2000)) togetherWith fadeOut(tween(2000))
                slideInHorizontally { 0 } togetherWith slideOutHorizontally { 0 }
                /*if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally(animationSpec = tween(2000)) { fullWidth -> fullWidth } togetherWith
                            slideOutHorizontally(animationSpec = tween(2000)) { fullWidth -> -fullWidth }
                } else {
                    slideInHorizontally(animationSpec = tween(2000)) { fullWidth -> -fullWidth } togetherWith
                            slideOutHorizontally(animationSpec = tween(2000)) { fullWidth -> fullWidth }
                }*/
            }) { select ->
            val sizePercent by transition.animateFloat(label = "2", transitionSpec = {
                tween(1000)

            }) { state ->
                if (state == EnterExitState.Visible) {
                    1f
                } else {
                    0f
                }
            }
            //shape需要通过remember 记录下才能动态效果
            val animatedShape = remember(sizePercent) {
                GenericShape { size, _ ->
                    addOval(
                        Rect(
                            Offset(
                                size.width / 2,
                                size.height / 2,
                            ), size.width * sizePercent
                        )
                    )
                }
            }
            if (select) {
                CardA(
                    modifier
                        .fillMaxWidth()
                        .clip(animatedShape)
                )
            } else {
                CardB(
                    modifier
                        .fillMaxWidth()
                        .clip(animatedShape)
                )
            }
        }
        Divider()
        Text(text = "AnimatedContent动画练习")
        Button(onClick = {
            selectA = !selectA
        }) {
            Text(text = if (selectA) "showB" else "showA")
        }
        var expandContentSize by remember {
            mutableStateOf(false)
        }
        val height = if (expandContentSize) 100.dp else 200.dp
        Divider()
        Text(text = "AnimateContentSize动画练习")
        Surface(color = MaterialTheme.colorScheme.primary) {
            Column(modifier = Modifier.animateContentSize(animationSpec = tween(2000))) {

                Text(text = "fdffddafda")
                if (expandContentSize) {
                    Text(
                        text = ("Composem ipsum color sit lazy, " +
                                "padding theme elit, sed do bouncy. ").repeat(4)
                    )
                }
            }
        }

        Button(onClick = {
            expandContentSize = !expandContentSize
        }) {
            Text(text = if (!expandContentSize) "expand" else "shrink")
        }

        Divider()
        Text(text = "AnimateAs*State动画练习 for color")
        var targetColor by remember {
            mutableStateOf(Color.Black)
        }
        val animateColor by animateColorAsState(
            targetValue = targetColor,
            animationSpec = tween(3000)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .blur(2.dp)
                .clip(MaterialTheme.shapes.large), color = animateColor
        ) {
            Box() {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "#" + animateColor.toArgb().toHexString().uppercase(),
                    color = animateColor.copy(
                        red = 1 - animateColor.red,
                        green = 1 - animateColor.green,
                        blue = 1 - animateColor.blue
                    )
                )
            }
        }
        Button(onClick = {
            val randomR = (0..10).random() / 10.0
            val randomG = (0..10).random() / 10.0
            val randomB = (0..10).random() / 10.0
            targetColor =
                Color((255 * randomR).toInt(), (255 * randomG).toInt(), (255 * randomB).toInt())
        }) {
            Text(text = "change Color")
        }

        Divider()
        Text(text = "Infinite动画练习 for color")
        var startInfiniteAnimation by remember {
            mutableStateOf(false)
        }
        if (startInfiniteAnimation) {
            val infiniteTransition = rememberInfiniteTransition()
            val infiniteAnimateColor by infiniteTransition.animateColor(
                initialValue = Color.White,
                targetValue = Color.Cyan,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        2000,
                        easing = LinearEasing
                    ), repeatMode = RepeatMode.Reverse
                )
            )
            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.large), color = infiniteAnimateColor) {
            }
        }
        Button(onClick = {
            startInfiniteAnimation = !startInfiniteAnimation
        }) {
            Text(text = (if (!startInfiniteAnimation) "start" else "stop") + " Infinite Change Color")
        }

        Divider()
        Text(text = "Animatable动画 练习 for color")
        var startAnimatable by remember {
            mutableStateOf(true)
        }
        val animatableColor = remember {
            Animatable(Color.Gray)
        }
        LaunchedEffect(startAnimatable) {
            animatableColor.animateTo(if (startAnimatable) Color.Cyan else Color.Magenta, animationSpec = tween(2000))
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(animatableColor.value))

        Button(onClick = {
            startAnimatable = !startAnimatable
        }) {
            Text(text = (if (!startAnimatable) "start" else "stop") + " Animatable Change Color")
        }

        Divider()
        Text(text = "手势结合动画练习 练习 for color")
        val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        //等待触摸事件
                        val position = awaitPointerEventScope {
                            awaitFirstDown().id
                        }
                        awaitPointerEventScope {
                            drag(position) {change ->
                                val dragOffset =  change.position
                                launch {
                                    offset.snapTo(dragOffset)
                                }
                                if (change.positionChange() != Offset.Zero) change.consume()
                            }
                        }

                    }
                }
            }) {
            Card(modifier = Modifier.size(50.dp).offset { (offset.value + Offset(25.dp.value, 25.dp.value)).toIntOffset()}, shape = CircleShape) {}
        }
    }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

@Composable
fun CardA(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(30.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {

    }
}

@Composable
fun CardB(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(30.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
    }
}