package com.project.feed.ui

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.feed.BUNDLE_USER_DETAILS
import com.project.feed.R
import com.project.feed.data.UserDetail
import com.project.feed.databinding.UserPostFragmentBinding
import com.project.feed.data.repository.Resource
import com.project.feed.network.connectivity.base.ConnectivityProvider
import com.project.feed.util.autoCleared
import com.project.feed.util.isSafeFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class UserPostFragment : BaseFragment(), UserPostAdapter.AdapterListener {

    private var binding: UserPostFragmentBinding by autoCleared()
    private val viewModel: UserPostViewModel by viewModels()

    private lateinit var infoAdapter: UserPostAdapter
    private lateinit var navController: NavController
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = UserPostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val searchEditText = searchView.findViewById<View>(R.id.search_src_text) as EditText
        searchEditText.hint = getString(R.string.user_id_search_hint)
        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                infoAdapter.filter.filter(s)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(view)
    }

    private fun setUpViews(view: View) {
        navController = Navigation.findNavController(view)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        infoAdapter = UserPostAdapter(this)
        binding.recyclerView.adapter = infoAdapter
    }

    private fun observeForData() {
        binding.textError.visibility = View.GONE
        viewModel.request.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    if (!it.data.isNullOrEmpty()) {
                        val filteredList = viewModel.getFilteredList(ArrayList(it.data))
                        infoAdapter.setItems(filteredList)
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

    override fun onClickedCharacter(userDetail: UserDetail) {
        val bundle = bundleOf(BUNDLE_USER_DETAILS to userDetail)
        navController.navigate(R.id.action_userPostFragment_to_userDetailFragment, bundle)
    }

    override fun onNoItemFound() {
        if (isSafeFragment(this)) {
            binding.recyclerView.visibility = View.GONE
            binding.textError.visibility = View.VISIBLE
            binding.textError.text = getString(R.string.no_data)
        }
    }

    override fun onItemFound() {
        if (isSafeFragment(this)) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textError.visibility = View.GONE
        }
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