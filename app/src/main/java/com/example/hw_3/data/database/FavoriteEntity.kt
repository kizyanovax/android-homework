package com.example.hw_3.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "item_index")
    val index: String,
    val name: String,
    val type: String, // "monster", "spell", "class"
    val jsonData: String // Serialized JSON data for offline access
)

