package com.pkk.sistembersepadusptbpkkhq.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkk.sistembersepadusptbpkkhq.feature.admin.AdminScreen
import com.pkk.sistembersepadusptbpkkhq.feature.approver.ApproverScreen
import com.pkk.sistembersepadusptbpkkhq.feature.auth.BiometricScreen
import com.pkk.sistembersepadusptbpkkhq.feature.auth.LoginScreen
import com.pkk.sistembersepadusptbpkkhq.feature.bakul.BakulScreen
import com.pkk.sistembersepadusptbpkkhq.feature.dashboard.DashboardScreen
import com.pkk.sistembersepadusptbpkkhq.feature.drive.DriveScreen
import com.pkk.sistembersepadusptbpkkhq.feature.excel.ExcelScreen
import com.pkk.sistembersepadusptbpkkhq.feature.formchecker.FormCheckerScreen
import com.pkk.sistembersepadusptbpkkhq.feature.history.HistoryScreen
import com.pkk.sistembersepadusptbpkkhq.feature.inputdb.InputDatabaseScreen
import com.pkk.sistembersepadusptbpkkhq.feature.pka.PkaScreen
import com.pkk.sistembersepadusptbpkkhq.feature.profile.ProfileScreen
import com.pkk.sistembersepadusptbpkkhq.feature.settings.SettingsScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.AUTH,
    onGoogleSignInSuccess: (String, String, String) -> Unit,
    onBiometricSuccess: () -> Unit,
    onBiometricFallback: () -> Unit,
    isLoggingIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.AUTH) {
            LoginScreen(
                onGoogleSignInSuccess = onGoogleSignInSuccess,
                isLoggingIn = isLoggingIn,
            )
        }

        composable(NavRoutes.BIOMETRIC) {
            BiometricScreen(
                onSuccess = onBiometricSuccess,
                onFallback = onBiometricFallback,
            )
        }

        composable(NavRoutes.MAIN) {
            MainScaffold(navController = navController)
        }

        composable(NavRoutes.DASHBOARD) {
            DashboardScreen()
        }

        composable(NavRoutes.FORM_CHECKER) {
            FormCheckerScreen()
        }

        composable(NavRoutes.INPUT_DATABASE) {
            InputDatabaseScreen()
        }

        composable(NavRoutes.APPROVER_VIEW) {
            ApproverScreen()
        }

        composable(NavRoutes.ADMIN) {
            AdminScreen()
        }

        composable(NavRoutes.PKA) {
            PkaScreen()
        }

        composable(NavRoutes.EXCEL) {
            ExcelScreen()
        }

        composable(NavRoutes.BAKUL) {
            BakulScreen()
        }

        composable(NavRoutes.HISTORY) {
            HistoryScreen()
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen()
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen()
        }

        composable(NavRoutes.DRIVE) {
            DriveScreen()
        }
    }
}

@Composable
fun ComingSoonScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("\uD83D\uDEA7", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Coming Soon", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
