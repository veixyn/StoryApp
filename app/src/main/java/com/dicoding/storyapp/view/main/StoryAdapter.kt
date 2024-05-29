package com.dicoding.storyapp.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.api.ListStoryItem
import com.dicoding.storyapp.databinding.ItemRvStoriesBinding
import com.dicoding.storyapp.view.main.storyDetail.StoryDetailActivity
import com.dicoding.storyapp.view.main.storyDetail.StoryDetailActivity.Companion.EXTRA_STORY

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemRvStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.imgStory)
            with(binding) {
                tvStoryTitle.text = story.name
                tvStoryDescription.text = story.description
            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding =
//            ItemRvStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val story = getItem(position)
//        holder.bind(story)
//        holder.itemView.setOnClickListener {
//            val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
//            intentDetail.putExtra(EXTRA_STORY, listStory[holder.adapterPosition])
//            holder.itemView.context.startActivity(intentDetail)
//        }
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
                holder.itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
                intentDetail.putExtra(EXTRA_STORY, data)
                holder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRvStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}