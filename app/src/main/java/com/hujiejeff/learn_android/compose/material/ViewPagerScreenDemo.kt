package com.hujiejeff.learn_android.compose.material

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.rotationMatrix
import com.google.android.material.math.MathUtils.lerp
import com.hujiejeff.learn_android.R
import kotlin.math.abs
import kotlin.math.absoluteValue

private val favoriteCollectionsData = listOf(
    R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
    R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
    R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
    R.drawable.fc4_self_massage to R.string.fc4_self_massage,
    R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
    R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPagerScreenDemo() {
    VerticalViewPagerDemo()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalViewPagerDemo(modifier: Modifier = Modifier) {
    val state = rememberPagerState(0, initialPageOffsetFraction = 0f) {
        favoriteCollectionsData.size
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
        ) { pageIndex ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scalePager(state, pageIndex)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = favoriteCollectionsData[pageIndex].drawable),
                    contentDescription = stringResource(id = favoriteCollectionsData[pageIndex].text),
                    contentScale = ContentScale.Crop
                )
                Text(text = stringResource(id = favoriteCollectionsData[pageIndex].text))
            }
        }
        state.currentPageOffsetFraction
        Text(
            text = "第${state.currentPage}页,currentFraction = ${state.currentPageOffsetFraction}\n, current",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalViewPagerDemo(modifier: Modifier = Modifier) {
    val state = rememberPagerState(0, initialPageOffsetFraction = 0f) {
        favoriteCollectionsData.size
    }
    var intSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = state,
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .height(1000.dp),
            pageSize = PageSize.Fixed(300.dp)
        ) { pageIndex ->
            Card(
                modifier = Modifier
                    .zIndex(10f - pageIndex)
                    .onGloballyPositioned {
                        intSize = it.size
                    }
                    .flyCardPager(state, pageIndex, intSize.height)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = favoriteCollectionsData[pageIndex].drawable),
                    contentDescription = stringResource(id = favoriteCollectionsData[pageIndex].text),
                    contentScale = ContentScale.Crop
                )
                Text(text = stringResource(id = favoriteCollectionsData[pageIndex].text))
            }
        }
        state.currentPageOffsetFraction
        Text(
            text = "第${state.currentPage}页,currentFraction = ${state.currentPageOffsetFraction}\n, height = ${intSize.height}",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

// 定义 Scale 效果
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.scalePager(pagerState: PagerState, page: Int): Modifier = this.then(
    onGloballyPositioned { coordinates ->

    }.graphicsLayer {
        val pageOffset = (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction
        transformOrigin = TransformOrigin(0.5f, 1f)
        rotationZ = pageOffset * 90
    }
)


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.flyCardPager(pagerState: PagerState, page: Int, height: Int): Modifier =
    this.then(graphicsLayer {
        // 0 ,1, 2
        val pageOffset = (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction
        scaleX = 1 - pageOffset * 0.2f
        scaleX = 1 - pageOffset * 0.2f
        if (pageOffset <= 0) {
            translationY = 0f
        } else {
            translationY = pageOffset * - height + (page - pagerState.currentPage) * 50
        }
    })

