package com.hujiejeff.learn_android.compose.codelab_animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationPracticeScreen(modifier: Modifier = Modifier) {
    var isShowCardA by remember {
        mutableStateOf(false)
    }
    var selectA by remember {
        mutableStateOf(true)
    }

    Column(modifier) {
        //AnimatedVisibility
        AnimatedVisibility(
            visible = isShowCardA,
            enter = slideIn(
                animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
            ) {
                IntOffset(0, -it.height)
            },
            exit = fadeOut(animationSpec = tween(durationMillis = 3000))
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
                        .clip(animatedShape))
            } else {
                CardB(
                    modifier
                        .fillMaxWidth()
                        .clip(animatedShape))
            }
        }
        Button(onClick = {
            selectA = !selectA
        }) {
            Text(text = if (selectA) "showB" else "showA")
        }
    }
}

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