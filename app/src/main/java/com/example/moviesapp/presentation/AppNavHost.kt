package com.example.moviesapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.List.name,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Screen.List.route) {
            MovieListScreen(
                onItemClick = {
                    navController.navigate("details/$it")
                })
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("movieId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry->
            val movieId = navBackStackEntry .arguments?.getInt("movieId")
            movieId?.let { MovieDetailsScreen(it) }
        }
    }
}

enum class Screen (val route: String) {
    List("list"),
    Details("details/{movieId}")
}