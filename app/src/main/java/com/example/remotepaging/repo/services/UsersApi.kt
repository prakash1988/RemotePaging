package com.example.remotepaging.repo.services


import com.example.remotepaging.model.User
import com.example.remotepaging.model.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {

    @GET("users/")
    suspend fun getAllUserData(@Query("page") page: Int): Response<UserList<User>>
}