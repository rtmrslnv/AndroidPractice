package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel

@Composable
fun Navigation(navController: NavHostController, viewModel: JobInfoViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainView(navController, viewModel)
        }
        composable("route2") {
            Text("Route 2", style = MaterialTheme.typography.titleLarge)
        }
        composable("route3") {
            Text("Route 3", style = MaterialTheme.typography.titleLarge)
        }
        composable("route4") {
            Text("Route 4", style = MaterialTheme.typography.titleLarge)
        }
        composable(
            "details/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val item = viewModel.jobInfos.value.find { it.id == backStackEntry.arguments?.getInt("itemId") };
            if (item != null) {
                JobInfoView(navController, item)
            }
        }
    }
}

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    navController: NavController,
    barColor: Color,
    onClick: (NavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntry
    NavigationBar(
        modifier = modifier,
        containerColor = barColor,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val isSelected = item.route == backStackEntry?.destination?.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Black
                ),
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = item.icon, contentDescription = item.text)
                        Text(
                            text = item.text,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(viewModel: JobInfoViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBar(
                items = listOf(
                    NavItem("main", "Main", Icons.Default.Home),
                    NavItem("route2", "WIP", Icons.Default.Person),
                    NavItem("route3", "WIP", Icons.Default.Star),
                    NavItem("route4", "WIP", Icons.Default.Search),
                ),
                navController = navController,
                onClick = {
                    navController.navigate(route = it.route) {
                        launchSingleTop = true
                    }
                },
                barColor = Color(255, 163, 26)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController, viewModel)
        }
    }
}