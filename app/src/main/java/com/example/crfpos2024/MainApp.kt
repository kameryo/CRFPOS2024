package com.example.crfpos2024

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature_goods.AddGoodsScreen
import com.example.feature_goods.AddGoodsViewModel
import com.example.feature_goods.GoodsListViewModel
import com.example.feature_goods.GoodsScreen
import com.example.feature_sales.SalesScreen
import com.example.feature_sales.SalesViewModel

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
            val viewModel: SalesViewModel = hiltViewModel()
            SalesScreen(
                viewModel = viewModel,
                back = {
                    navController.popBackStack()
                }
            )
        }

        composable("/record") {
//            RecordScreen()
        }

        composable("/goods") {
            val viewModel: GoodsListViewModel = hiltViewModel()
            GoodsScreen(
                back = {
                    navController.popBackStack()
                },
                toAddGoodsScreen = {
                    navController.navigate("/goods/add")
                },
                viewModel = viewModel,
            )
        }

        composable("/goods/add") {
            val viewModel: AddGoodsViewModel = hiltViewModel()
            AddGoodsScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }
    }

}