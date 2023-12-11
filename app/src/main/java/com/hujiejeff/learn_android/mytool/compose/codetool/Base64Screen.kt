package com.hujiejeff.learn_android.mytool.compose.codetool

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.hujiejeff.learn_android.mytool.compose.TabButtonGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Base64Screen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    val tabs = listOf(Base64.ENCODE, Base64.DECODE)
    var currentAction by remember {
        mutableStateOf(tabs[0])
    }

    Column(modifier.padding(16.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5,
            value = textInput,
            onValueChange = {
                textInput = it
            })
        Spacer(modifier = Modifier.height(8.dp * 2))
        TabButtonGroup(
            modifier = Modifier,
            defaultIndex = 0,
            size = tabs.size,
            getLabel = { index -> tabs[index].name },
            onSelectedListener = { index ->
                currentAction = tabs[index]
            })
        val result = textInput.let {
            when (currentAction) {
                Base64.ENCODE -> EncodeUtils.base64Encode2String(textInput.toByteArray())
                Base64.DECODE -> kotlin.runCatching { EncodeUtils.base64Decode(textInput) }.getOrNull()?.decodeToString() ?: ""
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = result,
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                ClipboardUtils.copyText(result)
                ToastUtils.showShort("Copied")
            }) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
        }
    }
}

enum class Base64(name: String) {
    ENCODE("Encode"),DECODE("Decode")
}