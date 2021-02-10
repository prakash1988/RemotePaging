package com.example.remotepaging.repo

import androidx.paging.PagingData
import com.example.remotepaging.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getAllUsers() :  Flow<PagingData<User>>
}