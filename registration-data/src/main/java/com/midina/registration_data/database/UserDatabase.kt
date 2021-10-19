package com.midina.registration_data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Annotates class to be a Room Database with a table (entity) of the Word class

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}