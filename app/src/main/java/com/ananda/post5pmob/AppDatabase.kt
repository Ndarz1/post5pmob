package com.ananda.post5pmob

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class, Story::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
	abstract fun postDao(): PostDao
	abstract fun storyDao(): StoryDao
	
	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null
		
		fun getDatabase(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"instagram_database"
				).build()
				INSTANCE = instance
				instance
			}
		}
	}
}