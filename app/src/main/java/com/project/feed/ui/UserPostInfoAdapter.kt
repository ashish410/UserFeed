package com.project.feed.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.feed.data.Post
import com.project.feed.databinding.ItemPostInfoBinding

class UserPostInfoAdapter : RecyclerView.Adapter<UserPostInfoViewHolder>() {

    private val items: MutableList<Post> = mutableListOf()
    private var email: String? = null
    private var userName: String? = null

    fun setItems(items: MutableList<Post>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setEmailAndUserName(email: String?, userName: String?) {
        this.email = email
        this.userName = userName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostInfoViewHolder {
        val binding =
            ItemPostInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserPostInfoViewHolder(binding, email, userName)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holderInfo: UserPostInfoViewHolder, position: Int) {
        val post = items[position]
        holderInfo.bindPost(post)
    }
}

class UserPostInfoViewHolder(
    private val postInfoBinding: ItemPostInfoBinding,
    private val email: String?,
    private val userName: String?
) :
    RecyclerView.ViewHolder(postInfoBinding.root), View.OnClickListener {

    private lateinit var post: Post

    init {
        postInfoBinding.root.setOnClickListener(this)
    }

    fun bindPost(post: Post) {
        this.post = post
        postInfoBinding.title.text = post.title
        postInfoBinding.body.text = post.body
        postInfoBinding.textUserEmail.text = email
        postInfoBinding.textUserName.text = userName
    }

    override fun onClick(v: View) {

    }
}