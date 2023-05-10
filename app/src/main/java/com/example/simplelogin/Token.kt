package com.example.simplelogin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tokens")
data class Token(

    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "phonenumber") val phonenumber: String?,
    @ColumnInfo(name = "refresh") val login: String?

): Serializable
