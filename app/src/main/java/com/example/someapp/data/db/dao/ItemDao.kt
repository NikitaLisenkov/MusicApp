package com.example.someapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.someapp.data.db.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("SELECT * FROM items")
    fun getAllItemsFlow(): Flow<List<ItemEntity>>
}