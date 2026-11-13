package com.example.campuscatalog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CatalogItemEntity::class], version = 1, exportSchema = false)
abstract class CatalogDatabase : RoomDatabase() {

    abstract fun catalogItemDao(): CatalogItemDao

    companion object {
        @Volatile
        private var INSTANCE: CatalogDatabase? = null

        // Singleton: dibuat sekali, TIDAK dibuat di Composable.
        fun getInstance(context: Context): CatalogDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CatalogDatabase::class.java,
                    "campus_catalog.db"
                ).build().also { INSTANCE = it }
            }
    }
}
