package com.uzair.composeBase.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.uzair.composeBase.ui.screens.launcherScreen.LauncherScreen
import com.uzair.composeBase.ui.screens.shipDetailsScreen.ShipDetailContainer
import com.uzair.composeBase.ui.screens.shipsScreen.ShipsScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationObjects.ScreenLauncher
    ) {

        composable<NavigationObjects.ScreenLauncher> {
            LauncherScreen {
                if (it == NavigateTo.ShipsScreen)
                    navController.navigate(NavigationObjects.ScreenShips) {
                        popUpTo(NavigationObjects.ScreenLauncher) { inclusive = true }
                    }
            }
        }

        composable<NavigationObjects.ScreenShips> {
            ShipsScreen { navigateTo, shipId ->
                if (navigateTo == NavigateTo.DetailScreen) {
                    navController.navigate(NavigationObjects.ScreenShipDetail(shipId))
                }
            }
        }

        composable<NavigationObjects.ScreenShipDetail> {
            val args=it.toRoute<NavigationObjects.ScreenShipDetail>()
            ShipDetailContainer(shipId = args.shipId) {

            }
        }
    }
}

enum class NavigateTo {
    ShipsScreen,
    LauncherScreen,
    DetailScreen,
}

sealed class NavigationObjects{
    @Serializable
    object ScreenLauncher

    @Serializable
    object ScreenShips

    @Serializable
    data class ScreenShipDetail(val shipId:String)

}
