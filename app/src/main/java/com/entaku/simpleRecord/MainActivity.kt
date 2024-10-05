package com.entaku.simpleRecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Recordings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Recordings.route) {
                RecordingsScreen(
                    onNavigateToRecordScreen = {
                        navController.navigate(Screen.Record.route)
                    }
                )
            }
            composable(Screen.Record.route) {
                RecordScreen()
            }
        }
    }
}
sealed class Screen(val route: String, val title: String) {
    object Recordings : Screen("recordings", "Recordings")
    object Record : Screen("record", "Record")
}
