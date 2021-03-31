package com.project.feed.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.project.feed.data.repository.DataRepository
import com.project.feed.data.UserDetail
import com.project.feed.data.repository.Resource

class UserDetailViewModel @ViewModelInject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    /**
     * Filters LiveData so that values will not be emitted unless they have changed.
     * Many times we might be notified about a change that does not contain any relevant changes.
     * If we’re listening for the user details, we will only update the UI when the user detail changes
     */
    fun requestUserInfo(userId: Int): LiveData<Resource<UserDetail>> =
        Transformations.distinctUntilChanged(dataRepository.getUserDetailById(userId))
}