package com.ananda.post5pmob

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {
	@Query("SELECT * FROM posts ORDER BY timestamp DESC")
	fun getAllPosts(): LiveData<List<Post>>
	
	@Insert
	suspend fun insertPost(post: Post)
	
	@Update
	suspend fun updatePost(post: Post)
	
	@Delete
	suspend fun deletePost(post: Post)
	
	@Query("SELECT * FROM posts WHERE id = :postId")
	suspend fun getPostById(postId: Int): Post?
}

@Dao
interface StoryDao {
	@Query("SELECT * FROM stories")
	fun getAllStories(): LiveData<List<Story>>
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStory(story: Story)
	
	@Query("DELETE FROM stories")
	suspend fun deleteAllStories()
}