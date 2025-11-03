package com.ananda.post5pmob

// Story.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class Story(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val username: String,
	val avatarResId: Int
)


@Entity(tableName = "posts")
data class Post(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val username: String,
	val avatarResId: Int,
	val imageUri: String,
	val caption: String,
	val timestamp: Long = System.currentTimeMillis()
)