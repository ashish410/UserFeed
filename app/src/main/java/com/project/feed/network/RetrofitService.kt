package com.project.feed.network

import com.project.feed.PATH_ID
import com.project.feed.ROUTE_POST
import com.project.feed.ROUTE_USERS
import com.project.feed.data.Post
import com.project.feed.data.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit helps in accessing remote data. We can see how to build a Retrofit object in AppModule.kt
 */
interface RetrofitService {
    @GET(ROUTE_POST)
    suspend fun getPostList(): Response<List<Post>>

    @GET(ROUTE_USERS)
    suspend fun getUserDetail(@Path(PATH_ID) id: Int): Response<UserDetail>
}