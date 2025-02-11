package com.example.someapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.someapp.data.db.dao.ItemDao
import com.example.someapp.data.db.entity.ItemEntity

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class ExampleDataBase : RoomDatabase() {

abstract fun itemDao(): ItemDao
}