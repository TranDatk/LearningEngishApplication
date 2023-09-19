package com.tnmd.learningenglishapp

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tnmd.learningenglishapp.screens.settings.SettingsScreen

import com.tnmd.learningenglishapp.common.composable.RationaleDialog
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.screens.chat.ChatScreen
import com.tnmd.learningenglishapp.screens.chat.MessageScreen
import com.tnmd.learningenglishapp.screens.extension.ExtensionScreen
import com.tnmd.learningenglishapp.screens.list_courses.CoursesScreen
import com.tnmd.learningenglishapp.screens.list_courses_quizz.CoursesQuizzScreen
import com.tnmd.learningenglishapp.screens.list_review.ReviewScreen
import com.tnmd.learningenglishapp.screens.list_words.WordsScreen
import com.tnmd.learningenglishapp.screens.login.LoginScreen
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
    val icons = listOf(
        R.drawable.home_stroke,
        R.drawable.openbook_stroke,
        R.drawable.extension_stroke,
        R.drawable.chat_stroke
    )

    val selectedIndex = remember { mutableStateOf(0) }
    val selectedIcon = if (selectedIndex.value < icons.size) icons[selectedIndex.value] else R.drawable.home_stroke

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        icons.forEachIndexed { index, iconId ->
            val isSelected = index == selectedIndex.value
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = if (isSelected) iconId else iconId),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    selectedIndex.value = index
                    when (index) {
                        0 -> navController.navigate(LIST_COURSES)
                        1 -> navController.navigate(LIST_COURSES_QUIZZ)
                        2 -> navController.navigate(EXTENSION_SCREEN)
                        3 -> navController.navigate(CHAT_SCREEN)
                    }
                }
            )
        }
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

    composable(CHAT_SCREEN) {
        ChatScreen(
            openScreen = { channelName ->
                appState.navigate("messageScreen/$channelName")
            }
        )
    }

    composable(LIST_COURSES_QUIZZ) {
        CoursesQuizzScreen( openAndPopUp = {route -> appState.navigate(route)} )
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(
        "messageScreen/{channelName}",
        arguments = listOf(navArgument("channelName") { type = NavType.StringType })
    ) { backStackEntry ->
        val channelName = backStackEntry.arguments?.getString("channelName")
        if (channelName != null) {
            // Truyền một hành động kết thúc vào MessageScreen
            MessageScreen(channelName = channelName)
        }
    }

    composable(
        route = "$LIST_WORDS$WORDS_ID_ARG",
        arguments = listOf(navArgument(COURSES_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        WordsScreen(
            openScreen = {route -> appState.navigate(route) }
        )
    }

    composable(
        route = "$LIST_REVIEW_SCREEN$WORDS_ID_ARG",
        arguments = listOf(navArgument(COURSES_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        ReviewScreen(
            openScreen = {route -> appState.navigate(route) }
        )
    }

    composable(EXTENSION_SCREEN) {
        ExtensionScreen()
    }
}

