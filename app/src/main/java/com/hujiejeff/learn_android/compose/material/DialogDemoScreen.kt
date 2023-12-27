package com.hujiejeff.learn_android.compose.material

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.blankj.utilcode.util.ScreenUtils
import com.hujiejeff.learn_android.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.invoke
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
    var currentShowIamge by remember { mutableStateOf<Int?>(null) }
    var currentShowIamgeContentScale by remember { mutableStateOf(ContentScale.Crop) }

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
        Spacer(modifier = Modifier.height(16.dp))
        Thumbnail(id = R.drawable.ab2_quick_yoga) { rect, contentScale ->
            isShowBigImageDialog = true
            smallRect = rect
            currentShowIamge = R.drawable.ab2_quick_yoga
            currentShowIamgeContentScale = contentScale
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            Thumbnail(id = R.drawable.ab2_quick_yoga) { rect, contentScale ->
                isShowBigImageDialog = true
                smallRect = rect
                currentShowIamge = R.drawable.ab2_quick_yoga
                currentShowIamgeContentScale = contentScale
            }
            Spacer(modifier = Modifier.width(16.dp))
            Thumbnail(id = R.drawable.ab1_inversions) { rect, contentScale ->
                isShowBigImageDialog = true
                smallRect = rect
                currentShowIamge = R.drawable.ab1_inversions
                currentShowIamgeContentScale = contentScale
            }
            Spacer(modifier = Modifier.width(16.dp))
            Thumbnail(id = R.drawable.ab3_stretching) { rect, contentScale ->
                isShowBigImageDialog = true
                smallRect = rect
                currentShowIamge = R.drawable.ab3_stretching
                currentShowIamgeContentScale = contentScale
            }
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
        ImageViewerDialog(initRect = smallRect!!, onDismiss = {
            isShowAlertDialog = false
            smallRect = null
        }, id = currentShowIamge!!, contentScale = currentShowIamgeContentScale)
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
fun Thumbnail(@DrawableRes id: Int, onClick: (Rect, ContentScale) -> Unit) {
    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val imageState = remember { mutableStateOf<ImageBitmap?>(null) }
    val resource = LocalContext.current.resources
    LaunchedEffect(id) {
        coroutineScope.launch(Dispatchers.IO) {
            val image = ImageBitmap.imageResource(resource, id) // 加载图片
            imageState.value = image  // 将结果保存在 State 中
        }
    }

    if (imageState.value != null) {
        assert(imageState.value != null)
        Image(
            bitmap = imageState.value!!,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .onGloballyPositioned {
                    //获取坐标
                    layoutCoordinates = it
                }
                .clickable {
                    layoutCoordinates?.let { lc ->
                        val globalPosition = lc.positionInWindow()
                        val size = lc.size.toSize()
                        val rect = Rect(Offset(globalPosition.x, globalPosition.y), size)
                        onClick(
                            rect,
                            if (imageState.value!!.height > imageState.value!!.width) ContentScale.FillWidth else ContentScale.FillHeight
                        )
                    }
                },
            contentScale = ContentScale.Crop
        )
    }

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

@Composable
fun ImageViewerDialog(onDismiss: () -> Unit, initRect: Rect, @DrawableRes id: Int, contentScale: ContentScale) {
    val intOffsetToVector: TwoWayConverter<IntOffset, AnimationVector2D> = TwoWayConverter(
        convertToVector = { offset: IntOffset ->
            AnimationVector2D(offset.x.toFloat(), offset.y.toFloat())
        },
        convertFromVector = { vector: AnimationVector2D ->
            IntOffset(vector.v1.toInt(), vector.v2.toInt())
        }
    )
    val isShow = remember {
        mutableStateOf(false)
    }
    val screenWidth = remember {
        ScreenUtils.getScreenWidth().toFloat()
    }
    val screenHeight = remember {
        ScreenUtils.getScreenHeight().toFloat()
    }
    val coroutineScope = rememberCoroutineScope()
    val initScale = initRect.width / screenWidth
    val dialogState = remember { DialogState() }
    var draggableOffset by remember { mutableStateOf(0f) }
    val scale = minOf(maxOf((1 - draggableOffset / 1000), 0.3f), 1f)
    var endDraggable by remember {
        mutableStateOf(false)
    }
    val startDraggable = remember(draggableOffset) {
        derivedStateOf { draggableOffset > 0 }
    }

    val imageViewerState =
        remember(dialogState.isShow, draggableOffset, startDraggable.value, endDraggable) {
            if (startDraggable.value) {
                if (endDraggable) {
                    if (draggableOffset > 500) {
                        dialogState.handleBackPress()
                        ImageViewerState.CLICK_BEFORE
                    } else {
                        ImageViewerState.CLICK_AFTER
                    }
                    endDraggable = false
                    draggableOffset = 0f
                } else {
                    ImageViewerState.DRAG_ING
                }
            } else {
                if (dialogState.isShow) ImageViewerState.CLICK_AFTER else ImageViewerState.CLICK_BEFORE
            }
        }

    CustomDialog(onDismiss = onDismiss, dialogState = dialogState) {
        val transition = updateTransition(targetState = imageViewerState, label = "complex")
        val bg by transition.animateColor(label = "bg") {
            when (it) {
                ImageViewerState.CLICK_BEFORE -> Color.Black.copy(alpha = 0f)
                ImageViewerState.CLICK_AFTER -> Color.Black
                else -> Color.Black.copy(alpha = scale)

            }
        }
        val width by transition.animateDp(label = "width") {
            pxToDp(
                px = when (it) {
                    ImageViewerState.CLICK_BEFORE -> initRect.width
                    ImageViewerState.CLICK_AFTER -> screenWidth
                    else -> screenWidth * scale
                }
            )

        }
        val height by transition.animateDp(label = "height") {
            pxToDp(
                px = when (it) {
                    ImageViewerState.CLICK_BEFORE -> initRect.height
                    ImageViewerState.CLICK_AFTER -> screenHeight
                    else -> screenHeight * scale
                }
            )
        }
        val offset by transition.animateIntOffset(label = "offset") {
            when (it) {
                ImageViewerState.CLICK_BEFORE -> IntOffset(
                    initRect.left.toInt(),
                    initRect.top.toInt()
                )

                ImageViewerState.CLICK_AFTER -> IntOffset.Zero
                else -> IntOffset(
                    (screenWidth.toInt() - dp2Px(width.value).toInt()) / 2,
                    (screenHeight.toInt() - dp2Px(height.value).toInt()) / 2
                )
            }
        }


        /*var startResumeDraggable by remember {
            mutableStateOf(false)
        }
        val scale = maxOf((1 - draggableOffset / 1000), 0.3f)
        val resumeTransition =
            updateTransition(targetState = startResumeDraggable, label = "updateTransition")
        val resumeDraggableScale by resumeTransition.animateFloat(label = "scale2") {
            if (it) 1f else scale
        }
        val resumeDraggableOffset by resumeTransition.animateIntOffset(label = "offset2") {
            if (it) IntOffset.Zero else IntOffset(
                0,
                draggableOffset.toInt()
            )
        }*/

        //0 -> crop 1 -> fill_width 2->fill_height
        val ivContentScale by remember(width.value, height.value) {
            derivedStateOf {
                if (height.value == initRect.height) {
                    ContentScale.Crop
                } else {
                    contentScale
                }
            }
        }


        Box(modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(bg)
            }) {
            Image(
                modifier = Modifier
                    .size(width, height)
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        indication = null,
                        onClick = {
                            dialogState.handleBackPress()
                        })
                    .offset {
                        offset
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            draggableOffset += delta
                        }, onDragStopped = {
                            endDraggable = true
                        }
                    ),
                painter = painterResource(id = id),
                contentDescription = null,
                contentScale = ivContentScale
            )
            /*Image(
                modifier = Modifier
                    .size(pxToDp(initRect.width), pxToDp(initRect.height))
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        indication = null,
                        onClick = {
                            dialogState.handleBackPress()
                        })
                    .offset {
//                        offset
                        IntOffset(initRect.left.toInt(), initRect.top.toInt())
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            draggableOffset += delta
                        }, onDragStopped = {
                            endDraggable = true
                        }
                    ),
                painter = painterResource(id = id),
                contentDescription = null,
                contentScale = if (imageViewerState == ImageViewerState.CLICK_BEFORE) ContentScale.Crop else ContentScale.FillWidth
            )*/
        }
    }
}

enum class ImageViewerState {
    CLICK_BEFORE,
    CLICK_AFTER,
    DRAG_ING,
    DRAG_END_TRIGGER,
    DRAG_END_RESUME,
}

@Composable
fun pxToDp(px: Float) = with(LocalDensity.current) {
    px.toDp()
}

@Composable
fun dp2Px(dp: Float) = with(LocalDensity.current) {
    dp.dp.toPx()
}


@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    dialogState: DialogState,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {
            dialogState.setIsShow(false)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = false
        )
    ) {

        val bgAlpha = remember {
            Animatable(if (dialogState.isShow) 0.7f else 0f)
        }


        LaunchedEffect(dialogState.isShow) {
            coroutineScope.launch {
                bgAlpha.animateTo(if (dialogState.isShow) 0.7f else 0f)
                if (!dialogState.isShow) onDismiss.invoke()
            }
        }
        val activityWindow = getActivityWindow()
        val dialogWindow = getDialogWindow()
        SideEffect {
            if (activityWindow != null && dialogWindow != null && !dialogState.isBackPress && !dialogState.isShow) {
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
                dialogState.setIsShow(true)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
/*            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(bgAlpha.value))
                    .clickOutSideModifier(true) {
//                        handleBackPress.invoke()
                    }
            )*/
            content()
            BackHandler {
                dialogState.handleBackPress()
            }
        }
    }
}

class DialogState() {
    var isShow by mutableStateOf(false)
        private set
    var isBackPress by mutableStateOf(false)
        private set

    fun handleBackPress() {
        if (!isBackPress) {
            isBackPress = true
            isShow = false
        }
    }

    fun setIsShow(boolean: Boolean) {
        isShow = boolean
    }

    fun setIsBackPress(boolean: Boolean) {
        isBackPress = boolean
    }
}