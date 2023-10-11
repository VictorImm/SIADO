package com.example.siado.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM user WHERE name = :n) THEN 1 ELSE 0 END")
    fun isExist(n: String): Int

//    @Query("DELETE FROM user")
//    suspend fun clear()
}