package com.example.remotepaging.repo

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.remotepaging.db.dao.AppDb
import com.example.remotepaging.model.PageInfo
import com.example.remotepaging.model.PageKey
import com.example.remotepaging.model.User
import com.example.remotepaging.repo.services.UsersApi

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator (val service: UsersApi, val db: AppDb) : RemoteMediator<Int, User>() {
    private val userDAO = db.userDao()
    private val keyDao = db.pageKeyDao()
    var url : String = "https://reqres.in/api/users/?page="
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val remoteKey: PageKey? = db.withTransaction {
                        if (lastItem?.id != null) {
                            keyDao.getNextPageKey(lastItem.id)
                        } else null
                    }

                    if (remoteKey?.nextPageUrl == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    val uri = Uri.parse(remoteKey.nextPageUrl)
                    val nextPageQuery = uri.getQueryParameter("page")
                    nextPageQuery?.toInt()
                }
            }

            val response = service.getAllUserData(loadKey ?: 1)
            val resBody = response.body()
            var page = resBody?.page
            var prevousPage = if (resBody?.page!!>1) UserRepoImpl.pageURL+page?.minus(1) else null
            var nextPage = if (resBody.totalPages > resBody.page) UserRepoImpl.pageURL+resBody.page.plus(1) else null
            var pageInfo = PageInfo(resBody.total,resBody.page,nextPage,prevousPage)
            val episodes = resBody?.results
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    /*userDAO.clearAll()
                    keyDao.clearAll()*/
                }
                episodes?.forEach {
                    it.page = loadKey
                    keyDao.insertOrReplace(PageKey(it.id, pageInfo?.next))
                }
                episodes?.let { userDAO.insertAll(it) }

            }

            MediatorResult.Success(
                endOfPaginationReached = nextPage == null
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}