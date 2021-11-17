package com.android.bottomnavigationwihnavigationgraph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.bottomnavigationwihnavigationgraph.ui.theme.BottomNavigationWihNavigationGraphTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomNavigationWihNavigationGraphTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        bottomBar = {
            MyBottomBar(navController)
        }
    ) { padding ->
        //action route compose
        NavHost(
            navController = navController,
            startDestination = Screen.Home.root,
            modifier = Modifier
                .padding(padding)
        ){
            composable(
                Screen.Home.root
            ){
                HomeScreen()
            }

            composable(
                Screen.Profile.root
            ){
                ProfileScreen()
            }

            composable(
                Screen.Notifications.root
            ){
                NotificationScreen()
            }
        }

    }
}

val mainScreens = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Notifications
)

@Composable
fun MyBottomBar(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    BottomNavigation {
        for (screen in mainScreens) {
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.root } == true,
                onClick = {
                    navController.navigate(screen.root) {
                        popUpTo(
                            id = navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                    )
                },
                label = {
                    Text(text = screen.title)
                }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    PlaceHolderScreen(
        title = Screen.Home.title,
        icon = Screen.Home.icon
    )
}

@Composable
fun ProfileScreen() {
    PlaceHolderScreen(
        title = Screen.Profile.title,
        icon = Screen.Profile.icon
    )
}

@Composable
fun NotificationScreen() {
    PlaceHolderScreen(
        title = Screen.Notifications.title,
        icon = Screen.Notifications.icon
    )
}

@Composable
fun PlaceHolderScreen(
    title: String,
    icon: ImageVector,
    content : @Composable ColumnScope.() -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
        Icon(
            icon,
            title,
            modifier = Modifier
                .fillMaxSize()
        )

        content()
    }
}
