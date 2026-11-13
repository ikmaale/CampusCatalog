package com.example.campuscatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.campuscatalog.ui.CatalogViewModel
import com.example.campuscatalog.ui.screens.FormScreen
import com.example.campuscatalog.ui.screens.ListScreen
import com.example.campuscatalog.ui.screens.SettingsScreen
import com.example.campuscatalog.ui.theme.CampusCatalogTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CatalogViewModel by viewModels {
        CatalogViewModel.Factory((application as CampusCatalogApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            CampusCatalogTheme(darkTheme = state.preferences.darkMode) {
                CatalogNavHost(viewModel)
            }
        }
    }
}

@Composable
private fun CatalogNavHost(viewModel: CatalogViewModel) {
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "list") {

        // Destination 1: daftar data
        composable("list") {
            ListScreen(
                state = state,
                onAdd = { navController.navigate("form/-1") },
                onEdit = { id -> navController.navigate("form/$id") },
                onDelete = viewModel::deleteItem,
                onOpenSettings = { navController.navigate("settings") }
            )
        }

        // Destination 2: form tambah/edit
        composable(
            route = "form/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            val existing = state.items.firstOrNull { it.id == id }
            FormScreen(
                existing = existing,
                onSave = { title, category, description, credits ->
                    if (existing == null) {
                        viewModel.addItem(title, category, description, credits)
                    } else {
                        viewModel.updateItem(
                            existing.copy(
                                title = title.trim(),
                                category = category.trim(),
                                description = description.trim(),
                                credits = credits
                            )
                        )
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Destination 3: pengaturan (DataStore)
        composable("settings") {
            SettingsScreen(
                prefs = state.preferences,
                onDarkModeChange = viewModel::setDarkMode,
                onSortOrderChange = viewModel::setSortOrder,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
