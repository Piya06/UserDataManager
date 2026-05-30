package com.userdata.manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.userdata.manager.data.local.UserEntity
import com.userdata.manager.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewmodel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){
    private val searchQuery = MutableLiveData("")

    val users = searchQuery.switchMap { query ->

        if (query.isEmpty()) {
            userRepository.getAllUsers()
        } else {
            userRepository.searchUsers(query)
        }
    }

    fun search(query: String) {
        searchQuery.value = query
    }

    fun delete(user: UserEntity) =
        viewModelScope.launch {
            userRepository.delete(user)
        }

    fun deleteMultiple(ids: List<Int>) =
        viewModelScope.launch {
            userRepository.deleteMultiple(ids)
        }

    fun insert(user: UserEntity){
        viewModelScope.launch {
            userRepository.insert(user)
        }
    }

    fun update(user: UserEntity){
        viewModelScope.launch {
            userRepository.update(user)
        }
    }
}