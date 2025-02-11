package com.example.someapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("description")
    val description: String
)
