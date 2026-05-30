package com.userdata.manager.data.repository

import androidx.lifecycle.LiveData
import com.userdata.manager.data.local.UserDao
import com.userdata.manager.data.local.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {

    suspend fun insert(user: UserEntity){
        dao.insertUser(user)
    }

    suspend fun update(user: UserEntity){
        dao.updateUser(user)
    }

    suspend fun delete(user: UserEntity){
        dao.deleteUser(user)
    }

    fun getAllUsers() = dao.getAllUsers()

    fun searchUsers(query: String): LiveData<List<UserEntity>> {
        return dao.searchUsers(query)
    }
  //  fun searchUsers(query: String) = dao.searchUsers(query)

    suspend fun deleteMultiple(ids: List<Int>){
        dao.deleteMultiple(ids)
    }

}