package com.example.remotepaging.model



import com.google.gson.annotations.SerializedName


data class UserList<T>(
    @SerializedName("data")
    val `results`: List<User>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("support")
    val support: Support,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,

    val pageInfo: PageInfo
)