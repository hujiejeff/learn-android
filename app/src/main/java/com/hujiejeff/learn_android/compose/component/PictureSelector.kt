package com.hujiejeff.learn_android.compose.component

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.compose.LocalNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

const val ROUTE_NAME = "PictureSelectorScreen"

fun NavController.navToPictureSelectorScreen() {
    navigate(ROUTE_NAME)
}

fun NavGraphBuilder.pictureSelectorScreen() {
    composable(ROUTE_NAME) {
        PictureSelectorScreen()
    }
}


@Composable
fun PictureSelectorScreen(
    modifier: Modifier = Modifier,
    viewModel: PictureSelectorViewModel = viewModel()
) {
    val bucketList by viewModel.picturesStateFlow.collectAsState()
    val currentBucket by viewModel.currentBucketStateFlow.collectAsState()
    LaunchedEffect(Unit) {
        delay(500)
        if (bucketList.isEmpty()) {
            viewModel.loadPictures()
        }
    }
    val navController = LocalNavController.current
    val selectedList = remember(currentBucket) {
        mutableStateListOf<PictureData>()
    }
    val inLimit by remember(selectedList) {
        derivedStateOf {
            selectedList.size < 9
        }
    }
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier.fillMaxSize()) {
        PictureSelectorHeader(
            bucketName = currentBucket.name,
            selectedCount = selectedList.size,
            expanded = menuExpanded,
            onExpandMenu = { menuExpanded = it },
            onClickBack = {
                navController.navigateUp()
            })
        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(80.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(
                    items = currentBucket.list,
                    key = { item: PictureData -> item.id }) { itemData ->
                    val selectedIndex by remember {
                        derivedStateOf {
                            selectedList.indexOf(itemData) + 1
                        }
                    }
                    val canSelected by remember {
                        derivedStateOf {
                            selectedIndex > 0 || inLimit
                        }
                    }
                    SelectablePicture(
                        key = 0,
                        selectedIndex = selectedIndex,
                        data = itemData,
                        onClick = {},
                        onSelected = { selected, onSelectedKey ->
                            if (selected) {
                                selectedList.add(itemData)

                            } else {
                                selectedList.remove(itemData)
                            }
                        },
                        canSelected = canSelected
                    )
                }
                /*                items(100, key = { index -> index }) { index ->
                                    val selectedIndex = selectedList.indexOf(index) + 1
                                    SelectablePicture(
                                        key = index,
                                        selectedIndex = selectedIndex,
                                        data = "",
                                        onClick = {},
                                        onSelected = { selected, onSelectedKey ->

                                            if (selected) {
                                                selectedList.add(onSelectedKey)

                                            } else {
                                                selectedList.remove(onSelectedKey)
                                            }
                                        },
                                        canSelected = selectedIndex > 0 || inLimit
                                    )
                                }*/
            }

            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = menuExpanded) {
                    AlbumFolders(
                        bucketList = bucketList,
                        currentPictureBucket = currentBucket,
                        onSelected = {
                            viewModel.selectAlbum(it)
                            menuExpanded = false
                        })
                    BackHandler {
                        menuExpanded = false
                    }
                }
                if (menuExpanded) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Black.copy(alpha = 0.8f))
                        .clickable {
                            menuExpanded = false
                        })
                }
            }
        }

        PictureBottomBar()
    }
}

@Composable
fun PictureSelectorHeader(
    modifier: Modifier = Modifier,
    bucketName: String = "最近项目",
    selectedCount: Int = 0,
    expanded: Boolean = false,
    onExpandMenu: (Boolean) -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Surface {
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClickBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
                val tint = MaterialTheme.colorScheme.onSecondaryContainer
                Text(text = bucketName, color = tint)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                        .clickable {
                            onExpandMenu.invoke(!expanded)
                        },
                    imageVector = Icons.Filled.ArrowCircleDown,
                    contentDescription = null,
                    tint = tint
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            FilledTonalButton(
                modifier = Modifier.height(32.dp),
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = "发送(${selectedCount}/9)")
            }

        }
    }
}

@Composable
fun PictureBottomBar(modifier: Modifier = Modifier) {
    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "预览")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = false, onClick = { /*TODO*/ })
                Text(text = "原图")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = false, onClick = { /*TODO*/ })
                Text(text = "选择")
            }
        }
    }
}

@Composable
fun SelectablePicture(
    modifier: Modifier = Modifier,
    data: PictureData,
    key: Int = 0,
    selectedIndex: Int = 0,
    onClick: () -> Unit,
    onSelected: (Boolean, Int) -> Unit,
    canSelected: Boolean
) {
    var selected by remember {
        mutableStateOf(selectedIndex > 0)
    }

    val context = LocalContext.current
    val model = remember(data) {
        ImageRequest.Builder(context)
            .data(data.contentUri)
            .setParameter("isThumb", true).build()
    }
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                onClick.invoke()
            }) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        /*Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ab2_quick_yoga),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )*/
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.1f))
        )
        AnimatedVisibility(visible = selectedIndex > 0, enter = fadeIn(), exit = fadeOut()) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.5f))
            )
        }
        SelectorCount(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .clip(CircleShape)
                .clickable {
                    if (canSelected) {
                        onSelected(selectedIndex <= 0, key)
                    }
                },
            index = selectedIndex
        )
    }
}

@Composable
fun AlbumFolders(
    modifier: Modifier = Modifier,
    currentPictureBucket: PictureBucket,
    bucketList: List<PictureBucket>,
    onSelected: (PictureBucket) -> Unit
) {
    Surface() {
        LazyColumn(modifier = modifier) {
            items(items = bucketList, key = { it.id }) { item ->
                AlbumFolderItem(
                    pictureBucket = item,
                    isSelected = currentPictureBucket == item,
                    onClick = {
                        onSelected.invoke(item)
                    })
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun AlbumFolderItem(
    modifier: Modifier = Modifier,
    pictureBucket: PictureBucket,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(64.dp),
            model = pictureBucket.list.first().contentUri,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        /*        Image(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(R.drawable.ab2_quick_yoga),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )*/
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = pictureBucket.name)
        Text(text = "(${pictureBucket.list.size})", color = Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SelectorCount(modifier: Modifier = Modifier, index: Int) {
    Box(
        modifier = modifier
            .size(25.dp)
            .clip(CircleShape),
    ) {

        if (index > 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = index.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.White, shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun SelectorCountPreview() {
    SelectorCount(modifier = Modifier, index = 2)
}

@Preview
@Composable
fun SelectorCountUnSelectedPreview() {
    SelectorCount(modifier = Modifier, index = 0)
}

@Preview
@Composable
fun SelectorPicturePreview() {
    var index by remember {
        mutableStateOf(0)
    }
    SelectablePicture(
        data = PictureData(0, "", Uri.EMPTY, "empty", "empty"),
        key = index,
        selectedIndex = index,
        onClick = {},
        onSelected = { boolean, key ->
            if (boolean) {
                index += 1
            } else {
                index -= 1
            }
        },
        canSelected = true
    )
}

@Preview
@Composable
fun PictureSelectorHeaderPreview() {
    PictureSelectorHeader()
}

@Preview
@Composable
fun PictureBottomBarPreview() {
    PictureBottomBar()
}

@Preview
@Composable
fun AlbumFoldersPreview() {
//    AlbumFolders()
}

@Composable
@Preview
fun PictureSelectorScreenPreview() {
    PictureSelectorScreen()
}