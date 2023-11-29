package com.hujiejeff.learn_android.mytool.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hujiejeff.learn_android.mytool.MyToolViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hujiejeff.learn_android.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun APPListScreen(viewModel: MyToolViewModel = viewModel()) {
    val appList by viewModel.appInfoList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var currentAPPInfo by remember {
        mutableStateOf<MyToolViewModel.APPInfo?>(null)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    if (appList.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.sendIntent(MyToolViewModel.Intent.LoadAPPList)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (appList.isEmpty()) {
            LottieLoading()
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true
            ) {
                items(items = appList, key = { info -> info.packageName }) {
                    PackageItemInfo(appInfo = it, onClick = {
                        currentAPPInfo = it
                        showBottomSheet = true
                    })
                }
            }
        }

        if (showBottomSheet) {
            PackItemClickModal(
                sheetState = sheetState,
                appInfo = currentAPPInfo!!,
                onHide = {
                    showBottomSheet = false
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackItemClickModal(
    sheetState: SheetState,
    appInfo: MyToolViewModel.APPInfo,
    onHide: () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onHide,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.height(200.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(40.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium),
                    model = appInfo.drawable,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = appInfo.label,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        onHide.invoke()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun PackageItemInfo(
    modifier: Modifier = Modifier,
    appInfo: MyToolViewModel.APPInfo,
    onClick: (MyToolViewModel.APPInfo) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke(appInfo)
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                modifier = Modifier
                    .height(32.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                model = appInfo.drawable,
                contentDescription = null
            )
            Text(text = appInfo.label)
        }
    }
}

@Composable
fun LottieLoading() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        modifier = Modifier.size(80.dp),
        composition = composition,
        progress = { progress },
    )
}