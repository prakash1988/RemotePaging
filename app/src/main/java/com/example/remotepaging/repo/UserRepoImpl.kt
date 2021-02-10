package com.example.remotepaging.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.remotepaging.db.dao.AppDb
import com.example.remotepaging.model.User
import com.example.remotepaging.repo.services.UsersApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepoImpl @Inject constructor(private val service: UsersApi,private val db: AppDb) : UserRepo{

    companion object{
        const val BASE_URL = "https://reqres.in/api/"
        const val pageURL = "https://reqres.in/api/users/?page="
    }


    @ExperimentalPagingApi
    override fun getAllUsers(): Flow<PagingData<User>> = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 2),
        remoteMediator = UserRemoteMediator(service, db)
    ) {
        db.userDao().pagingSource()
    }.flow


}