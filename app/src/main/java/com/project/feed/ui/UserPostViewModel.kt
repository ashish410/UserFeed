package com.project.feed.ui

import com.project.feed.data.repository.DataRepository
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.project.feed.data.Post
import com.project.feed.data.UserDetail

class UserPostViewModel @ViewModelInject constructor(dataRepository: DataRepository) : ViewModel() {

    val request = Transformations.distinctUntilChanged(dataRepository.getPostList())

    fun getFilteredList(mutableList: MutableList<Post>): MutableList<UserDetail> {
        val mutableMap: MutableMap<Int, MutableList<Post>> = mutableMapOf()
        for (post in mutableList) {
            val id = post.userId
            if (mutableMap.containsKey(id)) {
                var list = mutableMap[id]
                if (list.isNullOrEmpty()) {
                    list = mutableListOf()
                    list.add(post)
                    mutableMap[id] = list
                } else {
                    list.add(post)
                }
            } else {
                mutableMap[id] = mutableListOf()
            }
        }

        val mutableUserDetailList: MutableList<UserDetail> = mutableListOf()
        for ((key, value) in mutableMap) {
            mutableUserDetailList.add(UserDetail(key, value))
        }
        return mutableUserDetailList
    }
}