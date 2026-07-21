package com.pkk.sistembersepadusptbpkkhq.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkk.sistembersepadusptbpkkhq.feature.dashboard.DashboardScreen
import com.pkk.sistembersepadusptbpkkhq.feature.inbox.InboxScreen
import com.pkk.sistembersepadusptbpkkhq.feature.lagi.LagiScreen
import com.pkk.sistembersepadusptbpkkhq.feature.list.ApplicationListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(navController: NavHostController) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem("\uD83D\uDCCA", "Dashboard", NavRoutes.DASHBOARD),
        TabItem("\uD83D\uDCCB", "Senarai", NavRoutes.SENARAI),
        TabItem("\uD83D\uDCDD", "Borang", NavRoutes.FORM_CHECKER),
        TabItem("\uD83D\uDCE5", "Inbox", NavRoutes.INBOX),
        TabItem("\u2699\uFE0F", "Lagi", "lagi"),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SPTB (HQ)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Text(tab.icon, style = MaterialTheme.typography.titleLarge) },
                        label = { Text(tab.title) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            navController.navigate(tab.route) {
                                popUpTo(NavRoutes.DASHBOARD) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.DASHBOARD,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(NavRoutes.DASHBOARD) { DashboardScreen() }
            composable(NavRoutes.SENARAI) { ApplicationListScreen() }
            composable(NavRoutes.INBOX) { InboxScreen() }
            composable("lagi") {
                LagiScreen(onNavigate = { route -> navController.navigate(route) })
            }
        }
    }
}

data class TabItem(val icon: String, val title: String, val route: String)
