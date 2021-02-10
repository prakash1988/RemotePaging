package com.example.remotepaging.model

import com.google.gson.annotations.SerializedName

data class PagedResponse<T>(
    @SerializedName("data") val pageInfo: PageInfo,
    val results: List<T> = listOf()
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    var next: String?,
    val prev: String?
)