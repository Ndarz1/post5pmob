package com.ananda.post5pmob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter : ListAdapter<Story, StoryAdapter.StoryViewHolder>(StoryDiffCallback()) {
	
	inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val ivStoryAvatar: ImageView = itemView.findViewById(R.id.ivStoryAvatar)
		val tvStoryUsername: TextView = itemView.findViewById(R.id.tvStoryUsername)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_story, parent, false)
		return StoryViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
		val story = getItem(position)
		holder.tvStoryUsername.text = story.username
		holder.ivStoryAvatar.setImageResource(story.avatarResId)
	}
	
	class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
		override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
			return oldItem.id == newItem.id
		}
		
		override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
			return oldItem == newItem
		}
	}
}