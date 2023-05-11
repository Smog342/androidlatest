package com.example.simplelogin

import androidx.room.*

@Dao
interface TokenDAO {

    @Query("SELECT * FROM Tokens WHERE phonenumber = :phonenumber LIMIT 1")
    suspend fun getRefreshToken(phonenumber: String) : Token

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: Token)

    @Delete
    suspend fun deleteToken(token: Token)

    @Update
    suspend fun updateToken(token: Token)

}