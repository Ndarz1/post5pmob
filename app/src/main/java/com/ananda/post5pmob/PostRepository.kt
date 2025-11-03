package com.ananda.post5pmob

import androidx.lifecycle.LiveData

class PostRepository(private val postDao: PostDao, private val storyDao: StoryDao) {
	
	val allPosts: LiveData<List<Post>> = postDao.getAllPosts()
	val allStories: LiveData<List<Story>> = storyDao.getAllStories()
	
	suspend fun insertPost(post: Post) {
		postDao.insertPost(post)
	}
	
	suspend fun updatePost(post: Post) {
		postDao.updatePost(post)
	}
	
	suspend fun deletePost(post: Post) {
		postDao.deletePost(post)
	}
	
	suspend fun getPostById(postId: Int): Post? {
		return postDao.getPostById(postId)
	}
	
	suspend fun insertStory(story: Story) {
		storyDao.insertStory(story)
	}
	
	suspend fun deleteAllStories() {
		storyDao.deleteAllStories()
	}
}