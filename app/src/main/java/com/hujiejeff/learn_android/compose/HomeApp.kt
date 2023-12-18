package com.hujiejeff.learn_android.compose

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hujiejeff.learn_android.compose.codelab_animation.AnimationDemoNavi
import androidx.lifecycle.viewmodel.compose.viewModel


val LocalNavController = staticCompositionLocalOf<NavHostController> {
    noLocalProvidedFor("LocalNavController")
}

@Composable
fun getNavController(): NavHostController = LocalNavController.current


private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    //获取activity viewModel，默认情况下viewModel会去拿去navigation entry的viewModelStoreOwner
    viewModel: ComposeDemoViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Route.HomeRoute.route,
            enterTransition = { slideInHorizontally { fullWidth -> fullWidth } },
            exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } },
            popEnterTransition = { slideInHorizontally { fullWidth -> -fullWidth} },
            popExitTransition = { slideOutHorizontally { fullWidth -> fullWidth } }
        ) {
            composableWithRoute(route = Route.HomeRoute)
            composableWithRoute(route = Route.AnimateRoute.CodeLab)
            composableWithRoute(route = Route.AnimateRoute.Practice)
            composableWithRoute(route = Route.ChatListDemoRoute)
            composableWithRoute(route = Route.BasicLayoutDemo)
            composableWithRoute(route = Route.ComposeDemo2)
            composableWithRoute(route = Route.NavigationDemo)
            composableWithRoute(route = Route.MaterialDemo)
            composableWithRoute(route = Route.SearchViewDemo)
            composableWithRoute(route = Route.DialogDemo)
            /*composable(
                route = "demo" + "/{title}",
                arguments = listOf(navArgument("title") { type = NavType.StringType })
            ) {entry ->
                val title = entry.arguments?.getString("title")
                when(title) {
                    "code_lab_animation" -> AnimationDemo()

                }
            }*/

        }
    }

    //collect route to navigation
    val intent by viewModel.activityIntentFlow.collectAsState()
    LaunchedEffect(intent) {
        when(intent) {
            is ComposeDemoViewModel.Intent.NavigationIntent -> {
                navController.navigate((intent as ComposeDemoViewModel.Intent.NavigationIntent).route)
            }

            else -> {

            }
        }
    }
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: ComposeDemoViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    //Local.current必须出现在重组里面，通过赋值一下转化出来调用
    val navigatorController = LocalNavController.current
    Scaffold(modifier = modifier) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AnimationDemoNavi()
            FilledTonalButton(onClick = {
                viewModel.composeNavi(Route.ChatListDemoRoute)
            }) {
                Text(text = "聊天列表demo")
            }
            FilledTonalButton(onClick = {
                viewModel.composeNavi(Route.ComposeDemo2)
            }) {
                Text(text = "Compose Demo 2")
            }
            FilledTonalButton(onClick = {
                viewModel.composeNavi(Route.BasicLayoutDemo)
            }) {
                Text(text = "Compose Basic layout")
            }
            FilledTonalButton(onClick = {
                viewModel.composeNavi(Route.NavigationDemo)
            }) {
                Text(text = "Navigation Demo")
            }
            FilledTonalButton(onClick = {
                viewModel.composeNavi(Route.MaterialDemo)
            }) {
                Text(text = "Material Demo")
            }
            FilledTonalButton(onClick = {
//                viewModel.composeNavi(Route.SearchViewDemo)
//                clickRoute = Route.SearchViewDemo
                navigatorController.navigate(Route.SearchViewDemo.route)
            }) {
                Text(text = "Search Demo")
            }

            FilledTonalButton(onClick = {
                navigatorController.navigate(Route.DialogDemo.route)
            }) {
                Text(text = "DialogDemo Demo")
            }
        }
    }
}

fun NavGraphBuilder.composableWithRoute(route: Route) {
    composable(route.route) {
        route.composable.invoke()
    }
}