package com.dicoding.storyapp.data.api

import com.google.gson.annotations.SerializedName

data class StoryUploadResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,
)
