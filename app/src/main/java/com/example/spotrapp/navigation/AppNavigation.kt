package com.example.spotrapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotrapp.sampleItems
import com.example.spotrapp.ui.dashboard.DashboardScreen
import com.example.spotrapp.ui.history.HistoryScreen
import com.example.spotrapp.ui.onboarding.OnboardingScreen
import com.example.spotrapp.ui.putaway.AddItemScreen
import com.example.spotrapp.ui.putaway.CameraScanScreen
import com.example.spotrapp.ui.retrieval.EditItemScreen
import com.example.spotrapp.ui.retrieval.ItemDetailsScreen
import com.example.spotrapp.ui.search.SearchScreen
import com.example.spotrapp.ui.search.VoiceSearchScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var activeTutorialStep by remember { mutableStateOf<String?>(Routes.DASHBOARD) }

    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING,
        modifier = modifier
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onBoardingFinished = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onItemClick = { item -> navController.navigate(Routes.itemDetails(item.id)) },
                showTutorial = activeTutorialStep == Routes.DASHBOARD,
                onShowTutorial = { activeTutorialStep = Routes.DASHBOARD },
                onDismissTutorial = { activeTutorialStep = Routes.nextTutorialStep(Routes.DASHBOARD) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onScanClick = { navController.navigate(Routes.PUTAWAY) }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onHomeClick = { navController.navigate(Routes.DASHBOARD) },
                onScanClick = { navController.navigate(Routes.PUTAWAY) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onVoiceSearchClick = { navController.navigate(Routes.VOICE_SEARCH) },
                onItemClick = { item -> navController.navigate(Routes.itemDetails(item.id)) }
            )
        }

        composable(Routes.VOICE_SEARCH) {
            VoiceSearchScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                onHomeClick = { navController.navigate(Routes.DASHBOARD) },
                onScanClick = { navController.navigate(Routes.PUTAWAY) },
                onItemClick = { item -> navController.navigate(Routes.itemDetails(item.id)) }
            )
        }

        composable(Routes.PUTAWAY) {
            CameraScanScreen(
                onBackClick = { navController.popBackStack() },
                onScanClick = { navController.navigate(Routes.ADD_ITEM) },
                showTutorial = activeTutorialStep == Routes.PUTAWAY,
                onDismissTutorial = { activeTutorialStep = Routes.nextTutorialStep(Routes.PUTAWAY) }
            )
        }

        composable(Routes.ADD_ITEM) {
            AddItemScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = false }
                    }
                },
                showTutorial = activeTutorialStep == Routes.ADD_ITEM,
                onDismissTutorial = { activeTutorialStep = Routes.nextTutorialStep(Routes.ADD_ITEM) }
            )
        }

        composable(
            route = Routes.ITEM_DETAILS,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
            val item = sampleItems.find { it.id == itemId }
            if (item != null) {
                ItemDetailsScreen(
                    item = item,
                    onBack = { navController.popBackStack() },
                    onEditClick = { selectedItem -> navController.navigate(Routes.editItem(selectedItem.id)) },
                    onRetrieved = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                        }
                    },
                    onDeleted = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                        }
                    }
                )
            }
        }

        composable(
            route = Routes.EDIT_ITEM,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
            val item = sampleItems.find { it.id == itemId }
            if (item != null) {
                EditItemScreen(
                    item = item,
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}