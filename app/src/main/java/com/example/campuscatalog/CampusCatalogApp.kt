package com.example.campuscatalog

import android.app.Application
import com.example.campuscatalog.data.CatalogRepository
import com.example.campuscatalog.data.local.CatalogDatabase
import com.example.campuscatalog.data.prefs.UserPreferencesDataStore

class CampusCatalogApp : Application() {

    // Dibuat sekali di level Application (bukan di Composable).
    val repository: CatalogRepository by lazy {
        CatalogRepository(
            dao = CatalogDatabase.getInstance(this).catalogItemDao(),
            prefsStore = UserPreferencesDataStore(this)
        )
    }
}
