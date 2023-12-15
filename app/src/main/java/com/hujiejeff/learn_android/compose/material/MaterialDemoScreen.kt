package com.hujiejeff.learn_android.compose.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MaterialDemoScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            Surface(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f),
                shape = MaterialTheme.shapes.extraLarge.copy(topStart = CornerSize(0.dp))
            ) {
                Text(text = "ModalNavigationDrawer")
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Title") },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                        Box(modifier = Modifier) {
                            Surface(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(CircleShape)
                                    .clickable { }) {
                            }
                            BadgedBox(badge = {
                                Badge(modifier = Modifier.align(Alignment.TopEnd)) {
                                    Text(text = "100")

                                }
                            }, modifier = Modifier.padding(8.dp)) {
                                Icon(imageVector = Icons.Default.Monitor, contentDescription = null)

                            }

                        }



                        NavigationBarItem(
                            icon = {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            val badgeNumber = "8"
                                            Text(
                                                badgeNumber,
                                                modifier = Modifier.semantics {
                                                    contentDescription =
                                                        "$badgeNumber new notifications"
                                                }
                                            )
                                        }
                                    }) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Favorite"
                                    )
                                }
                            },
                            selected = false,
                            onClick = {}
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.AddBox, contentDescription = null)
                        }
                    })
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar("test message")
                    }
                }) {
                    Icon(imageVector = Icons.Default.Mail, contentDescription = null)
                }
            }) { padding ->
            Box(modifier = Modifier
                .padding(padding)) {
                Column(modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())) {
                    Text(text = "Button")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ElevatedButton(onClick = {}) {
                            Text(text = "ElevatedButton")
                        }
                        FilledTonalButton(onClick = {}) {
                            Text(text = "FilledTonalButton")
                        }
                        OutlinedButton(onClick = {}) {
                            Text(text = "OutlinedButton")
                        }
                        FilledTonalButton(onClick = {}) {
                            Text(text = "FilledTonalButton")
                        }
                        TextButton(onClick = {}) {
                            Text(text = "TextButton")
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                        FilledIconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                        OutlinedIconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                        val tooltipState: PlainTooltipState = remember { PlainTooltipState() }
                        PlainTooltipBox(
                            tooltipState = tooltipState,
                            tooltip = {
                                Text("Add to favorites")
                            },
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        tooltipState.show()
                                    }
                                }
                            ) {
                                Icon(tint = MaterialTheme.colorScheme.primary,
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Localized Description"
                                )
                            }
                        }
                    }


                    Divider()
                    Text(text = "FilterChip")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = {
                                Text("Chip1")},
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            })
                        FilterChip(selected = false, onClick = { /*TODO*/ }, label = {
                            Text("Chip2")
                        })
                    }

                    Divider()
                    Text(text = "Date Picker")
                    val datePickerState = rememberDatePickerState()
                    DatePicker(state = datePickerState)
                    Divider()
                }
            }
        }
    }

}

