package com.userdata.manager.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query(
        """
        SELECT * FROM users WHERE 
        name LIKE '%' || :query || '%' 
        OR
        phone LIKE '%' || :query || '%' 
        ORDER BY id DESC
    """
    ) fun searchUsers(query: String): LiveData<List<UserEntity>>

    @Query("DELETE FROM users WHERE id IN (:ids)")
    suspend fun deleteMultiple(ids: List<Int>)

}