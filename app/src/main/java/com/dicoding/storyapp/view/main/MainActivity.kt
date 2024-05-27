package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.MapsActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.api.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.addStory.UploadActivity
import com.dicoding.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var token: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.logout -> {
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("Log out?")
                        setMessage("Apakah anda yakin untuk log out?")
                        setPositiveButton("Ya") { _, _ ->
                            viewModel.logout()
                            val intent = Intent(context, WelcomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setNegativeButton("Tidak") { _, _ -> }
                        create()
                        show()
                    }
                    true
                }

                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                token = user.token
                viewModel.getStory(token)

                viewModel.story.observe(this) { stories ->
                    setStories(stories)
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
    }

    fun setStories(items: List<ListStoryItem>) {
        val adapter = StoryAdapter(items)
        adapter.submitList(items)
        binding.rvStories.adapter = adapter
    }
}