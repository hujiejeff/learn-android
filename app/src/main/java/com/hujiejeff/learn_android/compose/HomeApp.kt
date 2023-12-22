package com.hujiejeff.learn_android.compose

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
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
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hujiejeff.learn_android.compose.codelab_animation.AnimationDemoNavi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.hujiejeff.learn_android.compose.material.DialogDemo


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
            popEnterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
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
//            composableWithRoute(route = Route.DialogDemo)
            /*composable(
                "DialogDemo/{title}",
                arguments = listOf(navArgument("title") { NavType.StringType })
            ) {
                DialogDemo()
            }*/
            val uri = "example://dialog"
            composable(
                "${Route.DialogDemo.route}/{title}?arg={arg}",
                arguments = listOf(
                    navArgument("title") { NavType.StringType },
                    navArgument("arg") { defaultValue = ""}),//参数
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/{title}?arg={arg}" })//深层链接
            ) {
                DialogDemo()
            }
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
        when (intent) {
            is ComposeDemoViewModel.Intent.NavigationIntent -> {
                navController.navigate((intent as ComposeDemoViewModel.Intent.NavigationIntent).route) {
                    launchSingleTop = true
                }
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
    val viewModel: ComposeDemoViewModel =
        viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
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
            val context = LocalContext.current
            FilledTonalButton(onClick = {
//                viewModel.composeNavi(Route.SearchViewDemo)
//                clickRoute = Route.SearchViewDemo
                navigatorController.navigate(Route.SearchViewDemo.route)
            }) {
                Text(text = "Search Demo")
            }

            FilledTonalButton(onClick = {
                navigatorController.navigate(Route.DialogDemo.route + "/Dialog")
            }) {
                Text(text = "DialogDemo Demo")
            }

            FilledTonalButton(onClick = {
                context.testDeepLinkJump("fromCompose")
            }) {
                Text(text = "深层链接跳转")
            }
        }
    }
}

fun NavGraphBuilder.composableWithRoute(route: Route) {
    composable(route.route) {
        route.composable.invoke()
    }
}

fun Context.testDeepLinkJump(arg: String) {
    val deepLinkIntent = Intent(
        Intent.ACTION_VIEW,
        "example://dialog/hello?arg=${arg}".toUri(),
        this,
        ComposeDemoActivity::class.java
    )
    startActivity(deepLinkIntent)
}