package com.libertosforever.note.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "shop_list_item")
data class ShopListItem(

    @PrimaryKey (autoGenerate = true)
    val id:Int?,

    @ColumnInfo (name = "name")
    val name: String,

    @ColumnInfo (name = "item_info")
    val itemInfo: String = "",

    @ColumnInfo (name = "item_checked")
    val itemChecked: Boolean = false,

    @ColumnInfo (name = "listId")
    val listId: Int,

    @ColumnInfo (name = "itemType")
    val itemType: Int = 0
)







