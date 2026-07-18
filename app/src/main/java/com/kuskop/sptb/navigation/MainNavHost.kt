package com.kuskop.sptb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuskop.sptb.core.domain.User
import com.kuskop.sptb.feature.auth.BiometricScreen
import com.kuskop.sptb.feature.auth.LoginScreen
import com.kuskop.sptb.feature.dashboard.DashboardScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    user: User?,
    onGoogleSignInSuccess: (String, String, String) -> Unit,
    onBiometricSuccess: () -> Unit,
    onBiometricFallback: () -> Unit,
    isLoggingIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.AUTH
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
    }
}
