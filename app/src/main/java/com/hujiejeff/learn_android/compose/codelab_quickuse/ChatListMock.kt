package com.hujiejeff.learn_android.compose.codelab_quickuse

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hujiejeff.learn_android.R


@Composable
fun ChatListCodeLab(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        HelloMessage()
        MessageCard()
        Conversation(SampleData.conversationSample)
    }
}

@Preview
@Composable
fun HelloMessage() {
    Text(text = "hello world from function")
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MessageCard(msg: Message = Message("Jesse Pinkman", "How's it going!")) {

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.iv_avatar),
            contentDescription = "avatar",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Top)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember {
            mutableStateOf(true)
        }
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(text = msg.name, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Card(shape = ShapeDefaults.Large.copy(topStart = CornerSize(2.dp))) {
                Text(
                    text = msg.message,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}