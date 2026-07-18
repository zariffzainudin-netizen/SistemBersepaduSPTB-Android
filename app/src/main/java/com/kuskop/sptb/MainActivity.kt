package com.kuskop.sptb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuskop.sptb.core.ui.theme.SptbTheme
import com.kuskop.sptb.feature.auth.AuthViewModel
import com.kuskop.sptb.navigation.MainNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SptbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsState()
                    val isLoggingIn by authViewModel.isLoggingIn.collectAsState()

                    val user = when (val state = authState) {
                        is com.kuskop.sptb.feature.auth.AuthState.Authenticated -> state.user
                        else -> null
                    }

                    val startDest = when (authState) {
                        is com.kuskop.sptb.feature.auth.AuthState.NeedsBiometric -> com.kuskop.sptb.navigation.NavRoutes.BIOMETRIC
                        is com.kuskop.sptb.feature.auth.AuthState.NeedsGoogleSignIn -> com.kuskop.sptb.navigation.NavRoutes.AUTH
                        is com.kuskop.sptb.feature.auth.AuthState.Authenticated -> com.kuskop.sptb.navigation.NavRoutes.MAIN
                        else -> com.kuskop.sptb.navigation.NavRoutes.AUTH
                    }

                    com.kuskop.sptb.navigation.MainNavHost(
                        navController = androidx.navigation.compose.rememberNavController(),
                        user = user,
                        onGoogleSignInSuccess = { email, name, token ->
                            authViewModel.onGoogleSignInSuccess(email, name, token)
                        },
                        onBiometricSuccess = { authViewModel.onBiometricSuccess() },
                        onBiometricFallback = { authViewModel.onBiometricFallback() },
                        isLoggingIn = isLoggingIn,
                    )
                }
            }
        }
    }
}
