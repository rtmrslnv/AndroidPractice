package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.rtmrslnv.androidpractice.viewmodels.FavoritesViewModel
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel
import ru.rtmrslnv.androidpractice.viewmodels.ProfileViewModel
import ru.rtmrslnv.androidpractice.viewmodels.SettingsViewModel

@Composable
fun Navigation(navController: NavHostController, jobInfoViewModel: JobInfoViewModel,
               settingsViewModel: SettingsViewModel, favoritesViewModel: FavoritesViewModel,
               profileViewModel: ProfileViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainView(navController, jobInfoViewModel)
        }
        composable("settings") {
            SettingsView(navController, settingsViewModel)
        }
        composable("favorites") {
            FavoriesView(navController, favoritesViewModel)
        }
        composable("profile") {
            ProfileView(navController, profileViewModel)
        }
        composable("profileEdit") {
            ProfileEditView(navController, profileViewModel)
        }
        composable(
            "details/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("itemId");
            if (id != null)
            {
                val item = jobInfoViewModel.findJobInfo(id);
                if (item != null) {
                    JobInfoView(navController, item)
                }
            }
        }
        composable(
            "favorites/{favoriteId}",
            arguments = listOf(navArgument("favoriteId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("favoriteId");
            if (id != null)
            {
                val item = favoritesViewModel.findJobInfo(id);
                if (item != null) {
                    JobInfoView(navController, item)
                }
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
            NavigationBarItem(
                selected = false,
                onClick = { onClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Black
                ),
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.showBadge) {
                                Badge()
                            }
                        }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = item.icon, contentDescription = item.text)
                            Text(
                                text = item.text,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(jobInfoViewModel: JobInfoViewModel, settingsViewModel: SettingsViewModel,
               favoritesViewModel: FavoritesViewModel, profileViewModel: ProfileViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBar(
                items = listOf(
                    NavItem("main", "Main", Icons.Default.Home, false),
                    NavItem("settings", "Settings", Icons.Default.Settings, !settingsViewModel.isDefault()),
                    NavItem("favorites", "Favorites", Icons.Default.Star, false),
                    NavItem("profile", "Profile", Icons.Default.Person, false),
                ),
                navController = navController,
                onClick = {
                    navController.navigate(route = it.route) {
                        launchSingleTop = true
                    }
                },
                barColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController, jobInfoViewModel, settingsViewModel, favoritesViewModel, profileViewModel)
        }
    }
}