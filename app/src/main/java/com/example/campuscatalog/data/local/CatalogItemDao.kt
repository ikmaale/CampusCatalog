package com.example.campuscatalog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogItemDao {

    // Read berbasis Flow: UI otomatis diperbarui saat data berubah.
    @Query("SELECT * FROM catalog_items ORDER BY title ASC")
    fun getAllByTitle(): Flow<List<CatalogItemEntity>>

    @Query("SELECT * FROM catalog_items ORDER BY createdAt DESC")
    fun getAllByNewest(): Flow<List<CatalogItemEntity>>

    @Query("SELECT * FROM catalog_items ORDER BY credits DESC, title ASC")
    fun getAllByCredits(): Flow<List<CatalogItemEntity>>

    @Query("SELECT * FROM catalog_items WHERE id = :id")
    fun getById(id: Int): Flow<CatalogItemEntity?>

    @Insert
    suspend fun insert(item: CatalogItemEntity)

    @Update
    suspend fun update(item: CatalogItemEntity)

    @Delete
    suspend fun delete(item: CatalogItemEntity)
}
