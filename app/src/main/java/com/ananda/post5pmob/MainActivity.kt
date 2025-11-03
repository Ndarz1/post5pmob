package com.ananda.post5pmob

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
	
	private lateinit var rvStories: RecyclerView
	private lateinit var rvFeed: RecyclerView
	private lateinit var fabAddPost: FloatingActionButton
	private lateinit var storyAdapter: StoryAdapter
	private lateinit var postAdapter: PostAdapter
	private lateinit var viewModel: PostViewModel
	
	private var selectedImageUri: Uri? = null
	private var currentEditingPost: Post? = null
	
	private val imagePickerLauncher = registerForActivityResult(
		ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		uri?.let {
			selectedImageUri = it
			currentDialog?.findViewById<CardView>(R.id.cvImagePreview)?.visibility = View.VISIBLE
			currentDialog?.findViewById<ImageView>(R.id.ivImagePreview)?.let { imageView ->
				Glide.with(this).load(it).centerCrop().into(imageView)
			}
		}
	}
	
	private var currentDialog: Dialog? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		// Initialize ViewModel
		viewModel = ViewModelProvider(this)[PostViewModel::class.java]
		
		// Initialize Views
		rvStories = findViewById(R.id.rvStories)
		rvFeed = findViewById(R.id.rvFeed)
		fabAddPost = findViewById(R.id.fabAddPost)
		
		// Setup adapters
		setupStories()
		setupFeed()
		
		// Initialize default stories
		viewModel.initializeDefaultStories()
		
		// FAB click listener
		fabAddPost.setOnClickListener {
			showAddPostDialog()
		}
	}
	
	private fun setupStories() {
		storyAdapter = StoryAdapter()
		rvStories.apply {
			layoutManager = LinearLayoutManager(
				this@MainActivity,
				LinearLayoutManager.HORIZONTAL,
				false
			)
			adapter = storyAdapter
		}
		
		viewModel.allStories.observe(this) { stories ->
			storyAdapter.submitList(stories)
		}
	}
	
	private fun setupFeed() {
		postAdapter = PostAdapter { post ->
			showEditPostDialog(post)
		}
		rvFeed.apply {
			layoutManager = LinearLayoutManager(this@MainActivity)
			adapter = postAdapter
		}
		
		viewModel.allPosts.observe(this) { posts ->
			postAdapter.submitList(posts)
		}
	}
	
	private fun showAddPostDialog() {
		currentEditingPost = null
		selectedImageUri = null
		showPostDialog("Tambah Post Baru", null)
	}
	
	private fun showEditPostDialog(post: Post) {
		currentEditingPost = post
		selectedImageUri = Uri.parse(post.imageUri)
		showPostDialog("Edit Post", post)
	}
	
	private fun showPostDialog(title: String, post: Post?) {
		val dialog = Dialog(this)
		dialog.setContentView(R.layout.dialog_add_post)
		dialog.window?.setLayout(
			(resources.displayMetrics.widthPixels * 0.9).toInt(),
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT
		)
		dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
		
		currentDialog = dialog
		
		val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
		val etUsername = dialog.findViewById<TextInputEditText>(R.id.etUsername)
		val etCaption = dialog.findViewById<TextInputEditText>(R.id.etCaption)
		val btnAddImage = dialog.findViewById<Button>(R.id.btnAddImage)
		val cvImagePreview = dialog.findViewById<CardView>(R.id.cvImagePreview)
		val ivImagePreview = dialog.findViewById<ImageView>(R.id.ivImagePreview)
		val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
		
		tvTitle.text = title
		
		// Populate fields if editing
		post?.let {
			etUsername.setText(it.username)
			etCaption.setText(it.caption)
			cvImagePreview.visibility = View.VISIBLE
			Glide.with(this)
				.load(Uri.parse(it.imageUri))
				.centerCrop()
				.into(ivImagePreview)
		}
		
		btnAddImage.setOnClickListener {
			imagePickerLauncher.launch("image/*")
		}
		
		btnSubmit.setOnClickListener {
			val username = etUsername.text.toString().trim()
			val caption = etCaption.text.toString().trim()
			
			if (username.isEmpty()) {
				Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (caption.isEmpty()) {
				Toast.makeText(this, "Caption tidak boleh kosong", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (selectedImageUri == null) {
				Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			val avatarResId = getAvatarForUsername(username)
			
			if (currentEditingPost != null) {
				// Update existing post
				val updatedPost = currentEditingPost!!.copy(
					username = username,
					caption = caption,
					imageUri = selectedImageUri.toString(),
					avatarResId = avatarResId
				)
				viewModel.updatePost(updatedPost)
				Toast.makeText(this, "Post diperbarui", Toast.LENGTH_SHORT).show()
			} else {
				// Create new post
				val newPost = Post(
					username = username,
					avatarResId = avatarResId,
					imageUri = selectedImageUri.toString(),
					caption = caption
				)
				viewModel.insertPost(newPost)
				Toast.makeText(this, "Post ditambahkan", Toast.LENGTH_SHORT).show()
			}
			
			dialog.dismiss()
		}
		
		dialog.show()
	}
	
	private fun getAvatarForUsername(username: String): Int {
		return when (username.lowercase()) {
			"intan_dwi" -> R.drawable.avatar_1
			"minda_04" -> R.drawable.avatar_2
			"rubi_community" -> R.drawable.avatar_3
			"rizka" -> R.drawable.avatar_4
			"amanda" -> R.drawable.avatar_5
			else -> R.drawable.avatar_1
		}
	}
}