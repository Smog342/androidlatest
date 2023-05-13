package com.example.simplelogin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(

    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "phonenumber") val phonenumber: String?,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "role") val role: String?

) : Serializable
