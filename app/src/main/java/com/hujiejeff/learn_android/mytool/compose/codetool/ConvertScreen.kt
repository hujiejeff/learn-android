package com.hujiejeff.learn_android.mytool.compose.codetool

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils

@Preview
@Composable
fun ConvertScreen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf(0) }
    var currentAction by remember {
        mutableStateOf(Convert.Decimal)
    }


    Column() {
        NumberInput(convert = Convert.Decimal, value = textInput.toString(), onValueChange = {
            textInput = if (it.isNotEmpty()) {
                it.toInt()
            } else {
                0
            }
        })
        NumberInput(
            convert = Convert.Binary,
            value = Integer.toBinaryString(textInput),
            onValueChange = {
                if (it.isNotEmpty() && !it.matches(Regex("[0-1]+"))) {
                    return@NumberInput
                }
                if (it.isNotEmpty()) {
                    currentAction = Convert.Binary
                    textInput = it.toInt(2)
                } else {
                    textInput = 1
                }
            })
        NumberInput(
            convert = Convert.Octonary,
            value = Integer.toOctalString(textInput),
            onValueChange = {
                if (it.isNotEmpty() && !it.matches(Regex("[0-7]+"))) {
                    return@NumberInput
                }
                if (it.isNotEmpty()) {
                    currentAction = Convert.Hexadecimal
                    textInput = it.toInt(8)
                } else {
                    textInput = 0
                }
            })
        NumberInput(
            convert = Convert.Hexadecimal,
            value = Integer.toHexString(textInput),
            onValueChange = {
                if (it.isNotEmpty() && !it.matches(Regex("[0-9a-fA-F]+"))) {
                    return@NumberInput
                }
                if (it.isNotEmpty()) {
                    currentAction = Convert.Hexadecimal
                    textInput = it.toInt(16)
                } else {
                    textInput = 0
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInput(
    modifier: Modifier = Modifier, convert: Convert, value: String, onValueChange: (String) -> Unit
) {
    val keyboardOptions =
        KeyboardOptions.Default.copy(
            keyboardType = if (convert == Convert.Hexadecimal)
                KeyboardType.Text else KeyboardType.Number
        )
    Column(modifier = modifier.padding(16.dp)) {
        Row() {
            Text(text = convert.toString())
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                maxLines = 1,
                keyboardOptions = keyboardOptions
            )
            IconButton(onClick = {
                ClipboardUtils.copyText(value)
                ToastUtils.showShort("Copied")
            }) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
            }
        }

    }
}

enum class Convert {
    Decimal, Binary, Octonary, Hexadecimal
}