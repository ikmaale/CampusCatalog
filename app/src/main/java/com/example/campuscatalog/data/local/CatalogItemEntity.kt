package com.example.campuscatalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Room: satu baris = satu course di katalog kampus.
 * Memiliki 6 field (syarat minimal 4) dengan primary key auto-generate.
 */
@Entity(tableName = "catalog_items")
data class CatalogItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val credits: Int,
    val createdAt: Long = System.currentTimeMillis()
)
