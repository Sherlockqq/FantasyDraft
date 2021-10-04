package com.midina.registration_data.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity)

}