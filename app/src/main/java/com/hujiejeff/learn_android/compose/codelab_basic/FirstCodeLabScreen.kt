package com.hujiejeff.learn_android.compose.codelab_basic

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.compose.ui.theme.CustomizeTheme


@Composable
fun FirstCodeLabDemo() {
    var isDark by remember {
        mutableStateOf(false)
    }
    var isUseCustomTheme by remember {
        mutableStateOf(false)
    }
    /*if (isUseCustomTheme) {
        MyCustomTheme {
            FirstCodeLabScreen(
                onDarkThemeChange = { isDark = !isDark },
                onThemeChange = { isUseCustomTheme = !isUseCustomTheme })
        }
    } else {
        AppTheme(useDarkTheme = isDark) {
            FirstCodeLabScreen(
                onDarkThemeChange = { isDark = !isDark },
                onThemeChange = { isUseCustomTheme = !isUseCustomTheme })
        }
    }*/
    CustomizeTheme(useCustomTheme = isUseCustomTheme, useDarkTheme = isDark) {
        FirstCodeLabScreen(
            onDarkThemeChange = { isDark = !isDark },
            onThemeChange = { isUseCustomTheme = !isUseCustomTheme })
    }
}

@Preview
@Composable
fun FirstCodeLabScreen(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose"),
    onDarkThemeChange: () -> Unit = {},
    onThemeChange: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(onClick = onDarkThemeChange) {
                Text(text = "Change Dark Theme")
            }
            Button(onClick = onThemeChange) {
                Text(text = "Change Custom Theme")
            }
            Greetings()
        }
    }
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier, names: List<String> = List(100) { "$it" }
) {

    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(names) { name ->
            Greeting(name)
        }
    }
}

@Preview
@Composable
fun Greeting(name: String = "Name") {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 48.dp else 0.dp
    val extraPaddingAni by animateDpAsState(
        targetValue = extraPadding, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )
    Surface(color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(CircleShape.copy(all = CornerSize(16.dp)))
            .clickable {}) {
        Row(
            modifier = Modifier
                .padding(24.dp)
/*                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )*/
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPaddingAni.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello, ")
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                if (expanded.value) {
                    Text(
                        text = ("Composem ipsum color sit lazy, " +
                                "padding theme elit, sed do bouncy. ").repeat(4)
                    )
                }
            }/*     ElevatedButton(onClick = {
                     expanded.value = !expanded.value
                 }) {
                     Text(text = if (expanded.value) "Show less" else "Show more")
                 }*/
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(
                    imageVector = if (expanded.value) Icons.Filled.ExpandLess
                    else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded.value) stringResource(R.string.show_less)
                    else stringResource(
                        R.string.show_more
                    )
                )
            }
        }
    }
}