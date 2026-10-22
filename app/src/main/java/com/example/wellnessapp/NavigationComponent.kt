package com.example.wellnessapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.wellnessapp.data.JournalRepository
import com.example.wellnessapp.data.JournalRoomDatabase
import com.example.wellnessapp.ui.screens.*
import com.example.wellnessapp.viewmodel.*


object Routes {
    const val HOME = "home"
    const val CHAT = "chat"
    const val SOS = "sos"
    const val JOURNAL = "journal"
    const val PROFILE = "profile"

    const val DAILY_CHECKIN = "daily_checkin"
    const val DAILY_GOOD = "daily_good"
    const val DAILY_OK = "daily_ok"
    const val DAILY_BAD = "daily_bad"

    const val NEW_JOURNAL_ENTRY = "newJournalEntry"
    const val PANIC_ATTACK = "panicAttack"
    const val SUICIDAL_THOUGHTS = "suicidalThoughts"
    const val SELF_HARM_URGES = "selfHarmUrges"
    const val CALM_LIBRARY = "calmLibrary"
}


sealed class Screen(val route: String, @androidx.annotation.StringRes val labelRes: Int, val icon: ImageVector) {
    object Home : Screen(Routes.HOME, R.string.nav_home, Icons.Outlined.Home)
    object Chat : Screen(Routes.CHAT, R.string.nav_chat, Icons.Outlined.ChatBubbleOutline)
    object SOS : Screen(Routes.SOS, R.string.nav_sos, Icons.Outlined.Shield)
    object Journal : Screen(Routes.JOURNAL, R.string.nav_journal, Icons.AutoMirrored.Outlined.MenuBook)
    object Profile : Screen(Routes.PROFILE, R.string.nav_profile, Icons.Outlined.Person)
}


@Composable
fun NavigationComponent(
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val db = remember { JournalRoomDatabase.getDatabase(context) }
    val repo = remember { JournalRepository(db.journalDao()) }
    val journalViewModel: JournalViewModel = viewModel(
        factory = JournalViewModel.Factory(repo)
    )

    val chatViewModel: ChatViewModel = viewModel()

    val bottomBarScreens = listOf(
        Screen.Home,
        Screen.Chat,
        Screen.SOS,
        Screen.Journal,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            FigmaBottomBar(
                screens = bottomBarScreens,
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(navController)
            }
            composable(Routes.CHAT) {
                ChatScreen(navController, chatViewModel)
            }
            composable(Routes.SOS) {
                SosScreen(navController)
            }
            composable(Routes.JOURNAL) {
                JournalScreen(navController, journalViewModel)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    navController,
                    themeViewModel,
                    languageViewModel
                )
            }
            composable(Routes.DAILY_CHECKIN) {
                DailyCheckInScreen(
                    navController = navController,
                    journalViewModel = journalViewModel
                )
            }
            composable(Routes.DAILY_GOOD) {
                ResultGoodScreen(navController)
            }
            composable(Routes.DAILY_OK) {
                ResultOkayScreen(navController)
            }
            composable(Routes.DAILY_BAD) {
                ResultBadScreen(navController)
            }
            composable(Routes.NEW_JOURNAL_ENTRY) {
                NewJournalEntryScreen(navController, journalViewModel)
            }
            composable(Routes.PANIC_ATTACK) {
                PanicAttackScreen(navController)
            }
            composable(Routes.SUICIDAL_THOUGHTS) {
                SuicidalThoughtsScreen(navController)
            }
            composable(Routes.SELF_HARM_URGES) {
                SelfHarmUrgesScreen(navController)
            }
            composable(Routes.CALM_LIBRARY) {
                CalmLibraryScreen(navController)
            }
        }
    }
}
