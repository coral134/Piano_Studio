package com.example.pianostudio.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pianostudio.ui.screens.DrawHomeScreen
import com.example.pianostudio.ui.screens.DrawPracticeScreen
import com.example.pianostudio.ui.screens.DrawRecordScreen
import com.example.pianostudio.ui.screens.DrawSelectSongScreen
import com.example.pianostudio.ui.screens.DrawStudioOptionsScreen
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun Navigation(vm: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            DrawHomeScreen(
                modifier = Modifier.fillMaxSize(),
                nav = navController
            )
        }
        composable("practice") {
            DrawPracticeScreen(
                vm = vm,
                nav = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable("record") {
            DrawRecordScreen(
                vm = vm,
                nav = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable("select_song") {
            DrawSelectSongScreen(
                vm = vm,
                nav = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable("studio_options") {
            DrawStudioOptionsScreen(
                vm = vm,
                nav = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}