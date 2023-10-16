package com.example.siado.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM user WHERE name = :n AND status = :s) THEN 1 ELSE 0 END")
    suspend fun isExist(n: String, s: Int): Int

    @Query("DELETE FROM user")
    suspend fun clear()
}