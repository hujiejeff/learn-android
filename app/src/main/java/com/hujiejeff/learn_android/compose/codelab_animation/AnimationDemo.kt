package com.hujiejeff.learn_android.compose.codelab_animation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hujiejeff.learn_android.compose.ComposeDemoViewModel
import com.hujiejeff.learn_android.compose.Route

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimationDemoNavi(modifier: Modifier = Modifier) {
    val activityViewModel: ComposeDemoViewModel =
        viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    FlowRow(modifier.fillMaxWidth()) {
        FilledTonalButton(onClick = {
            activityViewModel.composeNavi(Route.AnimateRoute.CodeLab)
        }) {
            Text(text = "Google Animation CodeLab")
        }

        FilledTonalButton(onClick = {
            activityViewModel.composeNavi(Route.AnimateRoute.Practice)
        }) {
            Text(text = "Practise Animation Demo")
        }
    }
}
