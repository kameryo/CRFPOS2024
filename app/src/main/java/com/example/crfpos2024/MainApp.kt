package com.example.crfpos2024

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/") {
        composable("/") {
            MainScreen(
                toSalesScreen = {},
                toRecordScreen = {},
                toGoodsScreen = {}
            )
        }
    }

}