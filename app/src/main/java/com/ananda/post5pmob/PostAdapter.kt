package com.ananda.post5pmob

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(
	private val onEditClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {
	
	inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val ivPostAvatar: ImageView = itemView.findViewById(R.id.ivPostAvatar)
		val tvPostUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
		val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
		val tvPostCaption: TextView = itemView.findViewById(R.id.tvPostCaption)
		val ivPostMenu: ImageView = itemView.findViewById(R.id.ivPostMenu)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_post, parent, false)
		return PostViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
		val post = getItem(position)
		holder.tvPostUsername.text = post.username
		holder.tvPostCaption.text = post.caption
		holder.ivPostAvatar.setImageResource(post.avatarResId)
		
		// Load image from URI using Glide
		Glide.with(holder.itemView.context)
			.load(Uri.parse(post.imageUri))
			.centerCrop()
			.into(holder.ivPostImage)
		
		holder.ivPostMenu.setOnClickListener {
			onEditClick(post)
		}
	}
	
	class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
		override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
			return oldItem.id == newItem.id
		}
		
		override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
			return oldItem == newItem
		}
	}
}