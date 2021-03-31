package com.project.feed.data.repository

import com.project.feed.network.RemoteDataSource
import com.project.feed.network.performOperation
import javax.inject.Inject

class DataRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    fun getUserDetailById(id: Int) = performOperation(
        networkCall = { remoteDataSource.getUserDetail(id) }
    )

    fun getPostList() = performOperation(
        networkCall = { remoteDataSource.getPostList() }
    )
}