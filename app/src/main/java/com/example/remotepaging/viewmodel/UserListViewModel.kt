package com.example.remotepaging.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.remotepaging.base.BaseViewModel
import com.example.remotepaging.model.User
import com.example.remotepaging.repo.UserRepo
import kotlinx.coroutines.flow.Flow


class UserListViewModel @ViewModelInject constructor(private val userRepo: UserRepo):BaseViewModel(){

    private lateinit var _usersFlow: Flow<PagingData<User>>
    val usersFlow: Flow<PagingData<User>>
        get() = _usersFlow

    init {
        getAllUserList()
    }

    private fun getAllUserList() = launchPagingAsync({
        userRepo.getAllUsers().cachedIn(viewModelScope)
    }, {
        _usersFlow = it
    })
}