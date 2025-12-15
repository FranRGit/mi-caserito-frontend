package com.micaserito.app.ui.Main.Profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ItemDetails
import com.micaserito.app.ui.Viewholders.PostCardViewHolder

class PostListAdapter(
    private var posts: List<ItemDetails>
) : RecyclerView.Adapter<PostCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_card, parent, false)
        return PostCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostCardViewHolder, position: Int) {
        holder.render(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun updateData(newPosts: List<ItemDetails>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}