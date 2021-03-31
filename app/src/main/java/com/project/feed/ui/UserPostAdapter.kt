package com.project.feed.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.project.feed.R
import com.project.feed.data.UserDetail
import com.project.feed.databinding.ItemPostBinding
import kotlin.collections.ArrayList

class UserPostAdapter(private val adapterListener: AdapterListener) :
    RecyclerView.Adapter<UserPostViewHolder>(), Filterable {

    private var items: MutableList<UserDetail> = mutableListOf()
    private var itemsAll: MutableList<UserDetail> = mutableListOf()

    private var filter: Filter = object : Filter() {
        //runs on bg thread
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<UserDetail> = ArrayList()
            val searchText = constraint.toString()
            if (searchText.isEmpty()) {
                filteredList.addAll(itemsAll)
            } else {
                for (userDetail in itemsAll) {
                    if (userDetail.id.toString().contains(searchText)) {
                        filteredList.add(userDetail)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //runs on ui thread
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            items.clear()
            items.addAll(results.values as MutableList<UserDetail>)
            if (items.isEmpty()) adapterListener.onNoItemFound()
            else adapterListener.onItemFound()
            notifyDataSetChanged()
        }
    }

    interface AdapterListener {
        fun onClickedCharacter(userDetail: UserDetail)
        fun onNoItemFound()
        fun onItemFound()
    }

    fun setItems(items: MutableList<UserDetail>) {
        this.items = items
        this.itemsAll = ArrayList(items)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserPostViewHolder(binding, adapterListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holderInfo: UserPostViewHolder, position: Int) {
        val userDetail = items[position]
        holderInfo.bindPost(userDetail)
    }
}

class UserPostViewHolder(
    private val itemPostBinding: ItemPostBinding,
    private val listener: UserPostAdapter.AdapterListener
) :
    RecyclerView.ViewHolder(itemPostBinding.root), View.OnClickListener {

    private lateinit var userDetail: UserDetail

    init {
        itemPostBinding.root.setOnClickListener(this)
    }

    fun bindPost(userDetail: UserDetail) {
        this.userDetail = userDetail
        itemPostBinding.textCircleUserId.text = userDetail.id.toString()
        itemPostBinding.title.text = itemPostBinding.root.context.resources.getString(
            R.string.user_id,
            userDetail.id.toString()
        )
        itemPostBinding.body.text = itemPostBinding.root.context.resources.getString(
            R.string.no_of_post,
            userDetail.noOfPost.toString()
        )
    }

    override fun onClick(v: View) {
        listener.onClickedCharacter(userDetail)
    }
}