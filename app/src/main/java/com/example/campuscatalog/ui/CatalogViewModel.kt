package com.example.campuscatalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.campuscatalog.data.CatalogRepository
import com.example.campuscatalog.data.local.CatalogItemEntity
import com.example.campuscatalog.data.prefs.SortOrder
import com.example.campuscatalog.data.prefs.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CatalogUiState(
    val items: List<CatalogItemEntity> = emptyList(),
    val preferences: UserPreferences = UserPreferences(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(private val repository: CatalogRepository) : ViewModel() {

    // Room Flow + DataStore Flow digabung jadi satu UiState.
    // Saat sort order berubah di DataStore, query Room otomatis diganti (flatMapLatest).
    val uiState: StateFlow<CatalogUiState> = repository.preferences
        .flatMapLatest { prefs ->
            repository.items(prefs.sortOrder).map { items ->
                CatalogUiState(items = items, preferences = prefs, isLoading = false)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CatalogUiState()
        )

    fun itemById(id: Int): Flow<CatalogItemEntity?> = repository.itemById(id)

    fun addItem(title: String, category: String, description: String, credits: Int) {
        viewModelScope.launch {
            repository.add(
                CatalogItemEntity(
                    title = title.trim(),
                    category = category.trim(),
                    description = description.trim(),
                    credits = credits
                )
            )
        }
    }

    fun updateItem(item: CatalogItemEntity) {
        viewModelScope.launch { repository.update(item) }
    }

    fun deleteItem(item: CatalogItemEntity) {
        viewModelScope.launch { repository.delete(item) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { repository.setDarkMode(enabled) }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch { repository.setSortOrder(order) }
    }

    class Factory(private val repository: CatalogRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CatalogViewModel(repository) as T
    }
}
