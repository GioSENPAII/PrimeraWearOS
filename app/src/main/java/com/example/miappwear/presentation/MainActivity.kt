package com.example.miappwear.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.miappwear.presentation.screens.HealthDashboardScreen
import com.example.miappwear.presentation.screens.HydrationScreen
import com.example.miappwear.presentation.screens.StepsScreen
import com.example.miappwear.presentation.screens.SettingsScreen
import com.example.miappwear.presentation.theme.MiAppWearTheme
import com.example.miappwear.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Manejar resultados de permisos si es necesario
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        requestNecessaryPermissions()
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }

    private fun requestNecessaryPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BODY_SENSORS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}

@Composable
fun WearApp() {
    MiAppWearTheme {
        val navController = rememberSwipeDismissableNavController()
        val healthViewModel: HealthViewModel = viewModel()

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("dashboard") {
                HealthDashboardScreen(
                    viewModel = healthViewModel,
                    onNavigateToSteps = { navController.navigate("steps") },
                    onNavigateToHydration = { navController.navigate("hydration") },
                    onNavigateToSettings = { navController.navigate("settings") }
                )
            }

            composable("steps") {
                StepsScreen(
                    viewModel = healthViewModel,
                    onBackPressed = { navController.popBackStack() }
                )
            }

            composable("hydration") {
                HydrationScreen(
                    viewModel = healthViewModel,
                    onBackPressed = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = healthViewModel,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}