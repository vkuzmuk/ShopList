package com.libertosforever.note.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.libertosforever.note.entities.LibraryItem
import com.libertosforever.note.entities.NoteItem
import com.libertosforever.note.entities.ShoppingListItem
import com.libertosforever.note.entities.ShopListNameItem


@Database (entities = [
    LibraryItem :: class,
    NoteItem :: class,
    ShoppingListItem:: class,
    ShopListNameItem:: class], version = 1)

abstract class MainDatabase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase :: class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}