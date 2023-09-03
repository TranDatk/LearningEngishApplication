package com.tnmd.learningenglishapp

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.tnmd.learningenglishapp.screens.list_courses.CoursesScreen
import com.tnmd.learningenglishapp.screens.login.LoginScreen
import com.tnmd.learningenglishapp.screens.login.LoginViewModel
import com.tnmd.learningenglishapp.screens.sign_up.SignUpScreen
import com.tnmd.learningenglishapp.screens.splash.SplashScreen
import com.tnmd.learningenglishapp.ui.theme.LearningEnglishAppTheme
import kotlinx.coroutines.CoroutineScope


@Composable
@ExperimentalMaterialApi
fun LearningEnglishApp() {
    LearningEnglishAppTheme() {

        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberAppState()

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
                bottomBar = {
                    // Pass the navController to the bottom navigation composable
                    SootheBottomNavigation(navController = appState.navController)
                },
                scaffoldState = appState.scaffoldState,
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    learningEnglishGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        LearningEnglishAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
private fun SootheBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController // Inject the NavHostController
) {
    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null
                )
            },
            label = {
                androidx.compose.material3.Text(stringResource(R.string.bottom_navigation_home))
            },
            selected = true,
            onClick = {
                // Navigate to the desired screen when clicked
                navController.navigate(LIST_COURSES)
            }
        )
        NavigationBarItem(
            icon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                androidx.compose.material3.Text(stringResource(R.string.bottom_navigation_home))
            },
            selected = false,
            onClick = {
                // Navigate to the desired screen when clicked
                navController.navigate(SIGN_UP_SCREEN)
            }
        )
    }
}

@ExperimentalMaterialApi
fun NavGraphBuilder.learningEnglishGraph(appState: LearningEnglishAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndClear = { route -> appState.clearAndNavigate(route)  })
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(LIST_COURSES) {
        CoursesScreen( openAndPopUp = {route -> appState.navigate(route)} )
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

}

