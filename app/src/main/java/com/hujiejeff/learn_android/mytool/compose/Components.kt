package com.hujiejeff.learn_android.mytool.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hujiejeff.learn_android.mytool.compose.codetool.HashCode


@Preview
@Composable
fun TabButtonGroup(
    modifier: Modifier = Modifier,
    defaultIndex: Int = 0,
    onSelectedListener: (HashCode) -> Unit = {}
) {
    val tabs =
        listOf(HashCode.MD5, HashCode.SHA1, HashCode.SHA256, HashCode.SHA384, HashCode.SHA512)
    var selectedIndex by remember {
        mutableStateOf(defaultIndex)
    }
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.small)
            .shadow(20.dp),
        indicator = { tabPosition ->
            TabIndicator(code = tabs[selectedIndex], tabPosition = tabPosition)
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, item ->
            TabButton(selected = selectedIndex == index, item.name) {
                selectedIndex = index
                onSelectedListener.invoke(item)
            }
        }
    }
}

@Composable
fun TabButton(selected: Boolean, title: String = "title", onClick: () -> Unit = {}) {
    Tab(
        modifier = Modifier
            .height(32.dp)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.small)
            .zIndex(2f),
        selected = selected,
        onClick = onClick
    ) {
        Text(text = title, fontSize = 8.sp)
    }
}

@Composable
fun TabIndicator(code: HashCode, tabPosition: List<TabPosition>) {
    val transition = updateTransition(targetState = code, label = "Tab indicator")
    val indicatorLeft by transition.animateDp(label = "Indicator left", transitionSpec = {
        spring(stiffness = Spring.StiffnessMedium)
    }) { page ->
        tabPosition[page.ordinal].left
    }
    val indicatorRight by transition.animateDp(label = "Indicator right", transitionSpec = {
        spring(stiffness = Spring.StiffnessMedium)
    }) { page ->
        tabPosition[page.ordinal].right
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(2.dp)
            .fillMaxSize()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .shadow(0.dp)
    )
}