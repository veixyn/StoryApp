package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.addStory.UploadActivity
import com.dicoding.storyapp.view.maps.MapsActivity
import com.dicoding.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = StoryAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                lifecycleScope.launch {
                    setStories()
                    delay(1000)
                    (binding.rvStories.layoutManager as LinearLayoutManager).smoothScrollToPosition(binding.rvStories, null, 0)
                }
            }
        }

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
    }

    private fun setStories() {
        with(binding) {
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            refresh.setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.refresh()
                    delay(1000)
                    rvStories.layoutManager?.scrollToPosition(0)
                    refresh.isRefreshing = false
                }
            }
        }

        viewModel.story.observe(this) {
            if (it != null) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }
}