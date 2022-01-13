package com.libertosforever.note.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.libertosforever.note.entities.NoteItem
import com.libertosforever.note.entities.ShopListNameItem
import com.libertosforever.note.entities.ShopListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query ("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query ("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopListItemsByListId(listId: Int)

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertShopListName(nameItem: ShopListNameItem)

    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)


    @Update
    suspend fun updateNote(noteItem: NoteItem)

    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)

    @Update
    suspend fun updateListItem(item: ShopListItem)
}