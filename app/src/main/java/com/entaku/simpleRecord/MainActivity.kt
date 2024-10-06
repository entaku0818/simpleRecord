package com.entaku.simpleRecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.entaku.simpleRecord.db.AppDatabase

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
    val context = LocalContext.current

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Recordings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Recordings.route) {
                val database = remember { AppDatabase.getInstance(context) }
                val repository = remember { RecordingRepositoryImpl(database) }
                val viewModelFactory = remember { RecordingsViewModelFactory(repository) }
                val viewModel: RecordingsViewModel = viewModel(factory = viewModelFactory)

                val state by viewModel.uiState.collectAsState()

                RecordingsScreen(
                    state = state,
                    onNavigateToRecordScreen = {
                        navController.navigate(Screen.Record.route)
                    },
                    onRefresh = viewModel::loadRecordings
                )
            }
            composable(Screen.Record.route) {
                val database = remember { AppDatabase.getInstance(context) }
                val repository = remember { RecordingRepositoryImpl(database) }
                val viewModelFactory = remember { RecordViewModelFactory(repository) }
                val viewModel: RecordViewModel = viewModel(factory = viewModelFactory)
                val uiStateFlow = viewModel.uiState

                RecordScreen(
                    uiStateFlow = uiStateFlow,
                    onStartRecording = { viewModel.startRecording(context) },
                    onStopRecording = { viewModel.stopRecording() }
                )
            }

        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Recordings : Screen("recordings", "Recordings")
    object Record : Screen("record", "Record")
}
