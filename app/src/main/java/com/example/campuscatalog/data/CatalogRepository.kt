package com.example.campuscatalog.data

import com.example.campuscatalog.data.local.CatalogItemDao
import com.example.campuscatalog.data.local.CatalogItemEntity
import com.example.campuscatalog.data.prefs.SortOrder
import com.example.campuscatalog.data.prefs.UserPreferences
import com.example.campuscatalog.data.prefs.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Satu-satunya pintu masuk data layer.
 * ViewModel hanya berbicara dengan Repository, tidak pernah memanggil DAO langsung.
 */
class CatalogRepository(
    private val dao: CatalogItemDao,
    private val prefsStore: UserPreferencesDataStore
) {
    fun items(order: SortOrder): Flow<List<CatalogItemEntity>> = when (order) {
        SortOrder.TITLE -> dao.getAllByTitle()
        SortOrder.NEWEST -> dao.getAllByNewest()
        SortOrder.CREDITS -> dao.getAllByCredits()
    }

    fun itemById(id: Int): Flow<CatalogItemEntity?> = dao.getById(id)

    suspend fun add(item: CatalogItemEntity) = dao.insert(item)
    suspend fun update(item: CatalogItemEntity) = dao.update(item)
    suspend fun delete(item: CatalogItemEntity) = dao.delete(item)

    val preferences: Flow<UserPreferences> = prefsStore.preferences
    suspend fun setDarkMode(enabled: Boolean) = prefsStore.setDarkMode(enabled)
    suspend fun setSortOrder(order: SortOrder) = prefsStore.setSortOrder(order)
}
