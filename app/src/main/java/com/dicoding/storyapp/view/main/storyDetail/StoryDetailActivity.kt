package com.dicoding.storyapp.view.main.storyDetail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.api.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.storyapp.dateTimeFormatter

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.imgStoryDetail)
            with(binding) {
                tvStoryDetailCreatedAt.text = story.createdAt?.dateTimeFormatter()
                tvStoryDetailName.text = story.name
                tvStoryDetailDescriptionBody.text = story.description
            }
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}