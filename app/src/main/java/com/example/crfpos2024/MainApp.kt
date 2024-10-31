package com.example.crfpos2024

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature_goods.AddGoodsScreen
import com.example.feature_goods.AddGoodsViewModel
import com.example.feature_goods.EditGoodsScreen
import com.example.feature_goods.EditGoodsViewModel
import com.example.feature_goods.GoodsListViewModel
import com.example.feature_goods.GoodsScreen
import com.example.feature_record.EditRecordScreen
import com.example.feature_record.EditRecordViewModel
import com.example.feature_record.RecordScreen
import com.example.feature_record.RecordViewModel
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
            val viewModel: RecordViewModel = hiltViewModel()
            RecordScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
                toEdit = { id ->
                    navController.navigate("/record/$id")
                },
            )
        }

        composable("/record/{recordId}",
            arguments = listOf(
                navArgument("recordId") {
                    type = NavType.LongType
                }
            )
        ) {
            val viewModel: EditRecordViewModel = hiltViewModel()
            EditRecordScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
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
                toEdit = { id ->
                    navController.navigate("/goods/$id")
                },
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

        composable("/goods/{goodsId}",
            arguments = listOf(
                navArgument("goodsId") {
                    type = NavType.LongType
                }
            )
        ) {
            val viewModel: EditGoodsViewModel = hiltViewModel()
            EditGoodsScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }

    }

}