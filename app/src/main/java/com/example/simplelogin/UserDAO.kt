package com.example.simplelogin

import androidx.room.*
//import io.reactivex.rxjava3.core.Completable
//import io.reactivex.rxjava3.core.Maybe

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE login=:login AND password=:password LIMIT 1")
    suspend fun getUserForLog(login: String, password: String): User

    @Query("SELECT * FROM users WHERE login=:login LIMIT 1")
    suspend fun getUserForReg(login: String): User

    @Query("UPDATE users SET login = :newlogin, password = :password WHERE login = :login")
    suspend fun updateUserProfile(login: String, newlogin: String, password: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

}