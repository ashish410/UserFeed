package com.project.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.feed.BUNDLE_USER_DETAILS
import com.project.feed.R
import com.project.feed.data.UserDetail
import com.project.feed.databinding.UserDetailFragmentBinding
import com.project.feed.data.repository.Resource
import com.project.feed.network.connectivity.base.ConnectivityProvider
import com.project.feed.util.autoCleared
import com.project.feed.util.isSafeFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : BaseFragment() {

    private var binding: UserDetailFragmentBinding by autoCleared()
    private val viewModel: UserDetailViewModel by viewModels()

    private lateinit var infoAdapter: UserPostInfoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userDetail: UserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetail = arguments?.getParcelable(BUNDLE_USER_DETAILS)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        infoAdapter = UserPostInfoAdapter()
        infoAdapter.setItems(userDetail.allPostByUser)
        binding.recyclerView.adapter = infoAdapter
    }

    private fun observeForData() {
        binding.textError.visibility = View.GONE
        viewModel.requestUserInfo(userId = userDetail.id).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    it.data.let { userInfo ->
                        infoAdapter.setEmailAndUserName(userInfo?.email, userInfo?.name)
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.textError.visibility = View.VISIBLE
                    binding.textError.text = it.message
                }

                Resource.Status.LOADING -> {
                    if (binding.progressBar.visibility != View.VISIBLE)
                        binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        super.onStateChange(state)
        if (state.hasInternet()) {
            observeForData()
            return
        }
        handleNoInternet()
    }

    private fun handleNoInternet() {
        if (isSafeFragment(this)) {
            binding.progressBar.visibility = View.GONE
            if (binding.recyclerView.visibility == View.VISIBLE) {
                showSnackBar(
                    binding.root,
                    getString(R.string.snack_bar_no_internet),
                    Snackbar.LENGTH_SHORT
                )
                return
            }
            binding.textError.visibility = View.VISIBLE
            binding.textError.text = getString(R.string.text_no_internet)
        }
    }
}