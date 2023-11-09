package com.hujiejeff.learn_android.compose.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.internal.FlowLayout

class DemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NavigationDemo(modifier = Modifier)
            }
        }
    }


}

@Composable
fun NavigationDemo(modifier: Modifier) {
    val navController = rememberNavController()
    Column {
        Card(
            modifier
                .padding(16.dp)
                .height(200.dp)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Button(onClick = {
                    navController.navigate("RouteB")
                }) {
                    Text(text = "跳转")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        MyNavigationView(
            modifier = Modifier, navController = navController
        )
    }
}

@Composable
fun MyNavigationView(modifier: Modifier, navController: NavHostController) {
    NavHost(modifier = modifier, navController = navController, startDestination = "RouteA") {
        composable(route = "RouteA") {
            RouteA()
        }

        composable(route = "RouteB") {
            RouteB()
        }
    }
}

@Preview
@Composable
fun RouteA(modifier: Modifier = Modifier) {
    val dataTest = DataTest(2)
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "RouteA" + dataTest.number)
            Text(text = "BBBB", modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Preview
@Composable
fun RouteB(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "RouteB", modifier = Modifier.align(Alignment.BottomStart))
        }
    }
}

@Preview
@Composable
fun TestText() {
    Text(text = "Test Text", modifier = Modifier.firstBaselineToTOP(20.dp))
}

fun Modifier.firstBaselineToTOP(top: Dp) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val firstBaseLine = placeable[FirstBaseline]
    val placeableY = top.roundToPx() - firstBaseLine
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        placeable.placeRelative(0, placeableY)
    }
}

@Composable
fun MyBasicColumn(modifier: Modifier, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) {measurables, constraints ->
        //Measurement
        val placeables = measurables.map { measurable ->  measurable.measure(constraints)}

        //Layout
        layout(constraints.maxWidth, constraints.maxHeight) {
            var yPosition = 0
            placeables.forEach {placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}


data class DataTest(val number: Int? = null)