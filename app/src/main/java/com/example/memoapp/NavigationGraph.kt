package com.example.memoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.memoapp.view.AddNewMessageView
import com.example.memoapp.view.MainView
import com.example.memoapp.view.MemoView

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "main"){
        composable("main"){ MainView(navController) }
        composable("add"){ AddNewMessageView(navController) }
        composable(
            "memo?index={index}",
            arguments = listOf(
                navArgument("index") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index") ?: ""
            MemoView(navController, index.toInt())
        }
    }
}