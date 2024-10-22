package com.example.crfpos2024

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature_goods.GoodsScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/") {
        composable("/") {
            MainScreen(
                toSalesScreen = {
                    navController.navigate("/sales")
                },
                toRecordScreen = {
                    navController.navigate("/record")
                },
                toGoodsScreen = {
                    navController.navigate("/goods")
                }
            )
        }

        composable("/sales") {
//            SalesScreen()
        }

        composable("/record") {
//            RecordScreen()
        }

        composable("/goods") {
            GoodsScreen(
                back = {
                    navController.popBackStack()
                },
                toAddGoodsScreen = {
                    navController.navigate("/goods/add")
                }
            )
        }

        composable("/goods/add") {
//            AddGoodsScreen()
        }
    }

}