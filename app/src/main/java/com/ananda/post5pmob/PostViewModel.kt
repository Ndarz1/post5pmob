package com.ananda.post5pmob

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
	private val repository: PostRepository
	val allPosts: LiveData<List<Post>>
	val allStories: LiveData<List<Story>>
	
	init {
		val database = AppDatabase.getDatabase(application)
		val postDao = database.postDao()
		val storyDao = database.storyDao()
		repository = PostRepository(postDao, storyDao)
		allPosts = repository.allPosts
		allStories = repository.allStories
	}
	
	fun insertPost(post: Post) = viewModelScope.launch {
		repository.insertPost(post)
	}
	
	fun updatePost(post: Post) = viewModelScope.launch {
		repository.updatePost(post)
	}
	
	fun deletePost(post: Post) = viewModelScope.launch {
		repository.deletePost(post)
	}
	
	suspend fun getPostById(postId: Int): Post? {
		return repository.getPostById(postId)
	}
	
	fun insertStory(story: Story) = viewModelScope.launch {
		repository.insertStory(story)
	}
	
	fun initializeDefaultStories() = viewModelScope.launch {
		repository.deleteAllStories()
		val defaultStories = listOf(
			Story(username = "nafiah", avatarResId = R.drawable.avatar_1),
			Story(username = "nasya", avatarResId = R.drawable.avatar_2),
			Story(username = "ruby", avatarResId = R.drawable.avatar_3),
			Story(username = "rizka", avatarResId = R.drawable.avatar_4),
			Story(username = "amanda", avatarResId = R.drawable.avatar_5)
		)
		defaultStories.forEach { repository.insertStory(it) }
	}
}