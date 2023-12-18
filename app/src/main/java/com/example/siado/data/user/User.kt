package com.example.siado.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "status") // 0 = attend, 1 = go home
    val status: Int,
    @ColumnInfo(name = "arrival") // 0 = valid, 1 = late
    val arrival: Int,

    @ColumnInfo(name = "date")
    val date: Int,
    @ColumnInfo(name = "month")
    val mon: Int,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "hour")
    val hour: Int,
    @ColumnInfo(name = "minute")
    val min: Int,
    @ColumnInfo(name = "second")
    val sec: Int,
)