package com.kuskop.sptb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kuskop.sptb.core.ui.theme.SptbTheme
import com.kuskop.sptb.feature.auth.AuthState
import com.kuskop.sptb.feature.auth.AuthViewModel
import com.kuskop.sptb.navigation.MainNavHost
import com.kuskop.sptb.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SptbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsState()
                    val isLoggingIn by authViewModel.isLoggingIn.collectAsState()
                    val navController = remember { rememberNavController() }

                    val startDest = when (authState) {
                        is AuthState.NeedsBiometric -> NavRoutes.BIOMETRIC
                        is AuthState.Authenticated -> NavRoutes.MAIN
                        else -> NavRoutes.AUTH
                    }

                    MainNavHost(
                        navController = navController,
                        startDestination = startDest,
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
