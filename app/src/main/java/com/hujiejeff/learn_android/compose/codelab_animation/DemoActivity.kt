package com.hujiejeff.learn_android.compose.codelab_animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.compose.ui.theme.Amber600
import com.hujiejeff.learn_android.compose.ui.theme.AppTheme
import com.hujiejeff.learn_android.compose.ui.theme.Green300
import com.hujiejeff.learn_android.compose.ui.theme.Green800
import com.hujiejeff.learn_android.compose.ui.theme.Purple100
import com.hujiejeff.learn_android.compose.ui.theme.Purple700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationDemo()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDemo() {
    val allTasks = stringArrayResource(R.array.tasks)
    val allTopics = stringArrayResource(R.array.topics)
    var tabPage by remember { mutableStateOf(TabPage.Home) }
    val backgroundColor = if (tabPage == TabPage.Home) Purple100 else Green300
    val backgroundColorAnimate by animateColorAsState(targetValue = backgroundColor)
    var editMessageShown by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    var expandedTopic by remember {
        mutableStateOf<String?>(null)
    }

    suspend fun showEditMessage() {
        if (!editMessageShown) {
            editMessageShown = true
            delay(3000L)
            editMessageShown = false
        }
    }

    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = { HomeTopBar(tabPage, onSelected = { selected -> tabPage = selected }) },
            floatingActionButton = {
                HomeFloatingActionButton(extended = lazyListState.isScrollingUp(), onClick = {
                    coroutineScope.launch {
                        showEditMessage()
                    }
                })
            },
            containerColor = backgroundColorAnimate
        ) { padding ->
            Box {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        horizontal = 16.dp, vertical = 32.dp
                    ),
                    modifier = Modifier.padding(padding)
                ) {

                    item { Header(title = stringResource(R.string.weather)) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    //Weather
                    item {
                        Surface(shadowElevation = 2.dp) {
                            WeatherRow()
                        }
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }

                    //Topics
                    item { Header(title = stringResource(R.string.topics)) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(allTopics) { topic ->
                        TopicRow(
                            topic = topic,
                            expanded = expandedTopic == topic,
                            onClick = {
                                expandedTopic = if (expandedTopic == topic) null else topic
                            })
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }

                    //Tasks
                    item { Header(title = stringResource(R.string.tasks)) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(allTasks) { task ->
                        TaskRow(task = task)
                    }
                }
                Box(modifier = Modifier.padding(padding)) {
                    EditMessage(shown = editMessageShown)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeTopBar(tabPage: TabPage = TabPage.Home, onSelected: (TabPage) -> Unit = {}) {
    TabRow(selectedTabIndex = tabPage.ordinal, indicator = { tabPositions ->
        HomeTabIndicator(tabPosition = tabPositions, tabPage = tabPage)
    }) {
        HomeTab(icon = Icons.Default.Home, title = "Home", onClick = {
            onSelected(TabPage.Home)
        })
        HomeTab(icon = Icons.Default.Contacts, title = "Work", onClick = {
            onSelected(TabPage.Work)
        })
    }
}

private enum class TabPage {
    Home, Work
}

@Preview(showBackground = true)
@Composable
fun HomeTab(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Home,
    title: String = "Home",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}

@Composable
fun EditMessage(shown: Boolean) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(250, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            shadowElevation = 4.dp
        ) {
            Text(text = stringResource(R.string.edit_message), modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
private fun HomeTabIndicator(
    tabPosition: List<TabPosition>,
    tabPage: TabPage
) {
    val indicatorLeft = tabPosition[tabPage.ordinal].left
    val indicatorRight = tabPosition[tabPage.ordinal].right
    val color = if (tabPage == TabPage.Home) Purple700 else Green800
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(4.dp))
    )
}

@Composable
fun Header(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.semantics { heading() })
}

@Preview(showBackground = true)
@Composable
fun WeatherRow(onRefreshWeather: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Amber600)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(R.string.temperature), fontSize = 24.sp)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onRefreshWeather) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopicRow(topic: String = "Example Topic", expanded: Boolean = true, onClick: () -> Unit = {}) {
    TopicSpacer(shown = expanded)
    Surface(shadowElevation = 2.dp, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = topic, style = MaterialTheme.typography.bodyMedium)
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.lorem_ipsum), textAlign = TextAlign.Justify)
            }
        }
    }
    TopicSpacer(shown = expanded)
}

@Composable
fun TopicSpacer(shown: Boolean) {
    AnimatedVisibility(visible = shown) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun TaskRow(task: String = "First Task", onRemove: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = task)
        }
    }
}

@Preview
@Composable
fun HomeFloatingActionButton(extended: Boolean = true, onClick: () -> Unit = {}) {
    FloatingActionButton(onClick = onClick) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            AnimatedVisibility(visible = extended) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

