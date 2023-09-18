package com.tnmd.learningenglishapp

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tnmd.learningenglishapp.screens.settings.SettingsScreen
import com.tnmd.learningenglishapp.common.composable.PermissionDialog
import com.tnmd.learningenglishapp.common.composable.RationaleDialog
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.screens.login.LoginScreen
import com.tnmd.learningenglishapp.screens.sign_up.SignUpScreen
import com.tnmd.learningenglishapp.screens.splash.SplashScreen
import com.tnmd.learningenglishapp.ui.theme.LearningEnglishAppTheme
import kotlinx.coroutines.CoroutineScope
import com.tnmd.learningenglishapp.screens.*
import com.tnmd.learningenglishapp.screens.sign_up.SignUpScreenTwo


@Composable
@ExperimentalMaterialApi
fun LoginController() {
    LearningEnglishAppTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }

        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberLoginState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState,
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SETTINGS_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    loginGraph(appState)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}

@Composable
fun rememberLoginState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resourcesLogin(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        LearningEnglishAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resourcesLogin(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@RequiresApi(Build.VERSION_CODES.P)
@ExperimentalMaterialApi
fun NavGraphBuilder.loginGraph(appState: LearningEnglishAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndClear = { route -> appState.clearAndNavigate(route) })
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(SIGN_UP_SCREEN_TWO) {
        SignUpScreenTwo(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

}

