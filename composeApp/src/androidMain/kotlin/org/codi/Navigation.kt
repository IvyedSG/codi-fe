package org.codi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.codi.features.auth.LoginScreen

object Destinations {
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Destinations.HOME) {
                    popUpTo(Destinations.LOGIN) { inclusive = true }
                }
            })
        }
        composable(Destinations.HOME) {
            // HomeScreen()
        }
    }
}
