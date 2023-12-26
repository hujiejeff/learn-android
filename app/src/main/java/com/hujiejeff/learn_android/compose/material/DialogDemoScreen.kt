package com.hujiejeff.learn_android.compose.material

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.ScreenUtils
import com.hujiejeff.learn_android.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DialogDemo() {
    var isShowAlertDialog by remember {
        mutableStateOf(false)
    }
    var isShowCustomDialog by remember {
        mutableStateOf(false)
    }

    var isShowBigImageDialog by remember {
        mutableStateOf(false)
    }

    var smallRect by remember { mutableStateOf<Rect?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedButton(onClick = {
            isShowAlertDialog = true
        }) {
            Text(text = "OutlinedButton")
        }

        OutlinedButton(onClick = {
            isShowCustomDialog = true
        }) {
            Text(text = "ShowCustomDialog")
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = "测试", modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(text = "测试而", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }
        val myState = remember {
            MyState(0)
        }
        OutlinedButton(onClick = { myState.updateCount(myState.countState + 1) }) {
            Text(text = "${myState.countState}")
        }
        val viewModel: MaterialDemoViewModel = viewModel()

        Text(text = "title = " + viewModel.title)
        Text(text = "title = " + viewModel.arg)
        Spacer(modifier = Modifier.height(300.dp))
        Thumbnail() {
            isShowBigImageDialog = true
            smallRect = it
        }
    }

    if (isShowAlertDialog) {
        AlertDialogTest(onDismiss = {
            isShowAlertDialog = false
        })
    }

    if (isShowCustomDialog) {
        DialogFullScreen(onDismiss = {
            isShowCustomDialog = false
        })
    }

    if (isShowBigImageDialog && smallRect != null) {
        BigImageDialog(initRect = smallRect!!, onDismiss = {
            isShowAlertDialog = false
            smallRect = null
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogTest(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxSize(0.2f)) {
            Text(text = "title")
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
                    val rect = Rect(Offset(globalPosition.x, globalPosition.y), size)
                    onClick(rect)
                }
            },
        contentScale = ContentScale.Crop
    )
}


@Composable
private fun DialogFullScreen(onDismiss: () -> Unit) {
    var isShow by remember {
        mutableStateOf(false)
    }
    var isBackPress by remember {
        mutableStateOf(false)
    }
    val handleBackPress = {
        if (!isBackPress) {
            isBackPress = true
            isShow = false
        }
    }

    Dialog(
        onDismissRequest = {
            isShow = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = false
        )
    ) {
        val bgAlpha = remember {
            Animatable(if (isShow) 0f else 0.7f)
        }

        LaunchedEffect(isShow) {
            bgAlpha.animateTo(if (isShow) 0.7f else 0f)
            if (!isShow) onDismiss.invoke()
        }
        val activityWindow = getActivityWindow()
        val dialogWindow = getDialogWindow()
        SideEffect {
            if (activityWindow != null && dialogWindow != null && !isBackPress && !isShow) {
                val attributes = WindowManager.LayoutParams()
                // 复制Activity窗口属性
                attributes.copyFrom(activityWindow.attributes)
                // 这个一定要设置
                attributes.type = dialogWindow.attributes.type
                // 更新窗口属性
                dialogWindow.attributes = attributes
                // 设置窗口的宽度和高度，这段代码Dialog源码中就有哦，可以自己去查看
                dialogWindow.setLayout(
                    activityWindow.decorView.width,
                    activityWindow.decorView.height
                )
                isShow = true
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter
            /*.clickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null,
                onClick = {
                    isShow = false
                })
            .background(Color.Black.copy(bgAlpha.value))*/
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(bgAlpha.value))
                    .clickOutSideModifier(true) {
                        handleBackPress.invoke()
                    }
            )
            AnimatedVisibility(
                modifier = Modifier.pointerInput(Unit) {},
                visible = isShow,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Text(text = "Dialog")
                }
            }
            BackHandler() {
                handleBackPress.invoke()
            }
        }
    }
}

@Composable
private fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
private fun getActivityWindow(): Window? = LocalView.current.context.getActivityWindow()

private tailrec fun Context.getActivityWindow(): Window? = when (this) {
    is Activity -> window
    is ContextWrapper -> baseContext.getActivityWindow()
    else -> null
}

//禁用点击水波纹效果
class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

private fun Modifier.clickOutSideModifier(
    dismissOnClickOutside: Boolean,
    onTap: () -> Unit
) = this.then(
    if (dismissOnClickOutside) {
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                onTap()
            })
        }
    } else Modifier
)


class MyState(count: Int) {
    var countState by mutableStateOf(count)
        private set

    fun updateCount(newCount: Int) {
        countState = newCount
    }
}


@Composable
private fun BigImageDialog(onDismiss: () -> Unit, initRect: Rect) {
    val screenWidth = remember {
        ScreenUtils.getScreenWidth().toFloat()
    }
    val screenHeight = remember {
        ScreenUtils.getScreenHeight().toFloat()
    }
    val coroutineScope = rememberCoroutineScope()
    val initScale = initRect.width / screenWidth


    var isShow by remember {
        mutableStateOf(false)
    }
    var isBackPress by remember {
        mutableStateOf(false)
    }
    val handleBackPress = {
        if (!isBackPress) {
            isBackPress = true
            isShow = false
        }
    }

    Dialog(
        onDismissRequest = {
            isShow = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = false
        )
    ) {
        val offsetToVector: TwoWayConverter<Offset, AnimationVector2D> = TwoWayConverter(
            convertToVector = { offset: Offset ->
                AnimationVector2D(offset.x, offset.y)
            },
            convertFromVector = { vector: AnimationVector2D ->
                Offset(vector.v1, vector.v2)
            }
        )
        val bgAlpha = remember {
            Animatable(if (isShow) 0.7f else 0f)
        }
        val scale = remember {
            Animatable(if (isShow) 1f else initScale)
        }
        val width = remember {
            Animatable(if (isShow) screenWidth else initRect.width)
        }
        val height = remember {
            Animatable(if (isShow) screenHeight else initRect.height)
        }
        val leftTopOffset = Offset(initRect.left, initRect.top)
        val offset = remember {
            Animatable(if (isShow) Offset.Zero else leftTopOffset, offsetToVector)
        }
        LaunchedEffect(isShow) {
            coroutineScope.launch {
                bgAlpha.animateTo(if (isShow) 0.7f else 0f)
            }
            coroutineScope.launch {
                scale.animateTo(if (isShow) 1f else initScale)
            }
            coroutineScope.launch {
                width.animateTo(if (isShow) screenWidth else initRect.width)
            }
            coroutineScope.launch {
                height.animateTo(if (isShow) screenHeight else initRect.height)
            }
            coroutineScope.launch {
                offset.animateTo(if (isShow) Offset.Zero else leftTopOffset)
                if (!isShow) onDismiss.invoke()
            }
        }


        val activityWindow = getActivityWindow()
        val dialogWindow = getDialogWindow()
        SideEffect {
            if (activityWindow != null && dialogWindow != null && !isBackPress && !isShow) {
                val attributes = WindowManager.LayoutParams()
                // 复制Activity窗口属性
                attributes.copyFrom(activityWindow.attributes)
                // 这个一定要设置
                attributes.type = dialogWindow.attributes.type
                // 更新窗口属性
                dialogWindow.attributes = attributes
                // 设置窗口的宽度和高度，这段代码Dialog源码中就有哦，可以自己去查看
                dialogWindow.setLayout(
                    activityWindow.decorView.width,
                    activityWindow.decorView.height
                )
                isShow = true
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(bgAlpha.value))
                    .clickOutSideModifier(true) {
                        handleBackPress.invoke()
                    }
            )
            Image(
                modifier = Modifier
                    .width(with(LocalDensity.current) { width.value.toDp() })
                    .height(with(LocalDensity.current) { height.value.toDp() })
                    .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) },
                painter = painterResource(id = R.drawable.ab2_quick_yoga),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            BackHandler {
                handleBackPress.invoke()
            }
        }
    }
}