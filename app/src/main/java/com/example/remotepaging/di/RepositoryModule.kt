package com.example.remotepaging.di

import com.example.remotepaging.repo.UserRepo
import com.example.remotepaging.repo.UserRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {



    @Binds
    abstract fun bindUserRepository(
       userRepoImpl: UserRepoImpl
    ): UserRepo


}