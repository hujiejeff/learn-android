package com.hujiejeff.learn_android.mytool.compose.codetool

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeToolScreen(modifier: Modifier = Modifier) {
    var currentToolPage by remember {
        mutableStateOf(CodeToolPage.HASH)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = currentToolPage.title)
            })
        },
        bottomBar = {
            ToolNavigation(selectedItem = currentToolPage, onSelectListener = {
                currentToolPage = it
            })
        }) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            when (currentToolPage) {
                CodeToolPage.HASH -> HashScreen()
                CodeToolPage.BASE64 -> Base64Screen()
                CodeToolPage.QR -> QRScreen()
                CodeToolPage.CONVERT -> ConvertScreen()
            }
        }
    }
}

@Preview
@Composable
fun ToolNavigation(
    modifier: Modifier = Modifier,
    list: List<CodeToolPage> = listOf(
        CodeToolPage.HASH,
        CodeToolPage.BASE64,
        CodeToolPage.QR,
        CodeToolPage.CONVERT
    ),
    selectedItem: CodeToolPage = CodeToolPage.HASH,
    onSelectListener: (CodeToolPage) -> Unit = {}
) {
    NavigationBar(modifier = modifier) {
        list.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = {
                    onSelectListener.invoke(item)
                },
                label = { Text(text = item.title)},
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) })
        }
    }
}


enum class CodeToolPage(val title: String, val icon: ImageVector) {
    HASH("Hash", Icons.Default.Code),
    BASE64("Base64", Icons.Default.Tag),
    QR("QR", Icons.Default.QrCode),
    CONVERT("Convert", Icons.Default.Calculate)
}

