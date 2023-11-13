package com.hujiejeff.learn_android.mytool.compose.codetool

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.hujiejeff.learn_android.mytool.compose.TabButtonGroup
import java.util.Locale

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashScreen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    var currentHashCode by remember { mutableStateOf(HashCode.MD5) }
    var isShowUpperCase by remember { mutableStateOf(false) }
    val tabs =
        listOf(HashCode.MD5, HashCode.SHA1, HashCode.SHA256, HashCode.SHA384, HashCode.SHA512)

    Column(modifier.padding(16.dp)) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5,
            value = textInput,
            onValueChange = {
                textInput = it
            })
        Spacer(modifier = modifier.height(8.dp * 2))
        TabButtonGroup(
            modifier = Modifier,
            defaultIndex = 0,
            size = tabs.size,
            getLabel = { index -> tabs[index].name },
            onSelectedListener = { index ->
                currentHashCode = tabs[index]
            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "Show Upper Case")
            Switch(checked = isShowUpperCase, onCheckedChange = {
                isShowUpperCase = it
            })
        }
        val encodeResult = textInput.let {
            when (currentHashCode) {
                HashCode.MD5 -> EncryptUtils.encryptMD5ToString(it)
                HashCode.SHA1 -> EncryptUtils.encryptSHA1ToString(it)
                HashCode.SHA256 -> EncryptUtils.encryptSHA256ToString(it)
                HashCode.SHA384 -> EncryptUtils.encryptSHA384ToString(it)
                HashCode.SHA512 -> EncryptUtils.encryptSHA512ToString(it)
            }
        }.let {
            if (isShowUpperCase) it else it.toLowerCase()
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = encodeResult,
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                ClipboardUtils.copyText(encodeResult)
                ToastUtils.showShort("Copied")
            }) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
        }
    }
}

enum class HashCode(name: String) {
    MD5("MD5"), SHA1("SHA1"), SHA256("SHA256"), SHA384("SHA384"), SHA512("SHA512")
}