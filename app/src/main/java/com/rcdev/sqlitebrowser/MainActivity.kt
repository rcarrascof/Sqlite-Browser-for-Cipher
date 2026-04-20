package com.rcdev.sqlitebrowser

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rcdev.sqlitebrowser.ui.main.MainScreen
import com.rcdev.sqlitebrowser.ui.main.MainViewModel
import com.rcdev.sqlitebrowser.ui.tables.TablesScreen
import com.rcdev.sqlitebrowser.ui.tables.TablesViewModel
import com.rcdev.sqlitebrowser.ui.data.DataGridScreen
import com.rcdev.sqlitebrowser.ui.data.DataViewModel
import com.rcdev.sqlitebrowser.ui.query.QueryScreen
import com.rcdev.sqlitebrowser.ui.query.QueryViewModel
import com.rcdev.sqlitebrowser.ui.theme.SQLiteBrowserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkStoragePermission()

        setContent {
            SQLiteBrowserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            val viewModel: MainViewModel = hiltViewModel()
                            MainScreen(
                                viewModel = viewModel,
                                onDatabaseOpened = {
                                    navController.navigate("tables")
                                }
                            )
                        }
                        composable("tables") {
                            val viewModel: TablesViewModel = hiltViewModel()
                            TablesScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onTableSelected = { tableName ->
                                    navController.navigate("data/$tableName")
                                },
                                onOpenQueryConsole = {
                                    navController.navigate("query")
                                }
                            )
                        }
                        composable("query") {
                            val viewModel: QueryViewModel = hiltViewModel()
                            QueryScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("data/{tableName}") { backStackEntry ->
                            val viewModel: DataViewModel = hiltViewModel()
                            DataGridScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
            }
        }
    }
}
