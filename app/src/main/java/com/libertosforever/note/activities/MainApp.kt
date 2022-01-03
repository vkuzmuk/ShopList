package com.libertosforever.note.activities

import android.app.Application
import com.libertosforever.note.db.MainDatabase

class MainApp: Application() {
    val database by lazy { MainDatabase.getDatabase(this) }
}