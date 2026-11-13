package com.example.campuscatalog

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campuscatalog.data.local.CatalogDatabase
import com.example.campuscatalog.data.local.CatalogItemEntity
import com.example.campuscatalog.data.prefs.SortOrder
import com.example.campuscatalog.data.prefs.UserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Membuktikan data Room dan preference DataStore tetap ada
 * setelah database "ditutup" lalu "dibuka kembali" (simulasi restart).
 */
@RunWith(AndroidJUnit4::class)
class PersistenceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val dbName = "persistence_test.db"

    private fun openDb() =
        Room.databaseBuilder(context, CatalogDatabase::class.java, dbName).build()

    @Test
    fun roomData_survivesCloseAndReopen() = runBlocking {
        context.deleteDatabase(dbName)

        val db1 = openDb()
        db1.catalogItemDao().insert(
            CatalogItemEntity(title = "Pemrograman Bergerak", category = "Wajib",
                description = "Android", credits = 3)
        )
        db1.close() // simulasi aplikasi ditutup

        val db2 = openDb() // simulasi aplikasi dibuka kembali
        val items = db2.catalogItemDao().getAllByTitle().first()
        db2.close()
        context.deleteDatabase(dbName)

        assertEquals(1, items.size)
        assertEquals("Pemrograman Bergerak", items[0].title)
    }

    @Test
    fun dataStorePreferences_surviveReadAgain() = runBlocking {
        val store = UserPreferencesDataStore(context)
        store.setDarkMode(true)
        store.setSortOrder(SortOrder.CREDITS)

        // Instance baru = simulasi pembacaan setelah reopen
        val prefs = UserPreferencesDataStore(context).preferences.first()
        assertTrue(prefs.darkMode)
        assertEquals(SortOrder.CREDITS, prefs.sortOrder)

        store.setDarkMode(false)
        store.setSortOrder(SortOrder.TITLE)
    }
}
