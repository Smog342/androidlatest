package com.example.simplelogin

import androidx.room.*

interface TokenDAO {

    @Query("SELECT * FROM Tokens WHERE phonenumber = :phonenumber LIMIT 1")
    suspend fun getRefreshToken(phonenumber: String) : Token

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(token: Token)

    @Delete
    suspend fun deleteUser(token: Token)

    @Update
    suspend fun updateUser(token: Token)

}