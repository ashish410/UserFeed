package com.project.feed.network

import com.project.feed.data.repository.BaseDataSource
import javax.inject.Inject

/**
 * The suspend modifier is brought to us by Kotlin Coroutines,it indicates that the following function
 * will execute in a coroutine (similar to a thread) allowing us to keep the UI thread unblocked
 * while long lasting operations such as getting our data from the internet are being executed.
 */
class RemoteDataSource @Inject constructor(private val retrofitService: RetrofitService) :
    BaseDataSource() {
    suspend fun getPostList() = getResult { retrofitService.getPostList() }

    suspend fun getUserDetail(id: Int) = getResult { retrofitService.getUserDetail(id) }
}