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

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM user WHERE name = :n AND status = :s) THEN 1 ELSE 0 END")
    suspend fun isExist(n: String, s: Int): Int

    @Query("SELECT * FROM user WHERE name = :n AND status = :s")
    suspend fun findUser(n: String, s: Int): User

    @Query("SELECT * FROM user WHERE status = 0")
    fun getAttend(): Flow<List<User>>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM user WHERE name = :n AND status = 1) THEN 1 ELSE 0 END")
    suspend fun isGoHome(n: String): Int

    @Query("DELETE FROM user")
    suspend fun clear()
}