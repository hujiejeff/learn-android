package com.hujiejeff.learn_android.compose

import androidx.compose.runtime.Composable
import com.hujiejeff.learn_android.compose.codelab_animation.AnimationCodeLabScreen
import com.hujiejeff.learn_android.compose.codelab_animation.AnimationPracticeScreen
import com.hujiejeff.learn_android.compose.codelab_basic.FirstCodeLabDemo
import com.hujiejeff.learn_android.compose.codelab_basic_layout.BasicLayoutDemoScreenPortrait
import com.hujiejeff.learn_android.compose.codelab_quickuse.ChatListCodeLab
import com.hujiejeff.learn_android.compose.navigation.NavigationDemoScreen

sealed class Route(val route: String, val composable: @Composable () -> Unit) {
    object HomeRoute : Route("home", {
        HomeScreen()
    })

    open class AnimateRoute(route: String, composable: @Composable () -> Unit) :
        Route(route, composable) {
        object CodeLab : AnimateRoute("code_lab", {
            AnimationCodeLabScreen()
        })

        object Practice : AnimateRoute("practice", {
            AnimationPracticeScreen()
        })
    }

    object ChatListDemoRoute: Route("chat_list_demo", {
        ChatListCodeLab()
    })

    object ComposeDemo2: Route("FirstCodeLab", {
        FirstCodeLabDemo()
    })

    object BasicLayoutDemo: Route("BasicLayoutDemo", {
        BasicLayoutDemoScreenPortrait()
    })

    object NavigationDemo: Route("NavigationDemo", {
        NavigationDemoScreen()
    })
}