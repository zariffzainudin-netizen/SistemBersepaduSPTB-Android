package com.pkk.sistembersepadusptbpkkhq

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
import com.pkk.sistembersepadusptbpkkhq.core.ui.theme.SptbTheme
import com.pkk.sistembersepadusptbpkkhq.feature.auth.AuthState
import com.pkk.sistembersepadusptbpkkhq.feature.auth.AuthViewModel
import com.pkk.sistembersepadusptbpkkhq.navigation.MainNavHost
import com.pkk.sistembersepadusptbpkkhq.navigation.NavRoutes
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
                    val navController = rememberNavController()

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
