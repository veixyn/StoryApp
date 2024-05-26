package com.dicoding.storyapp.view.main.addStory

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.databinding.ActivityUploadBinding
import com.dicoding.storyapp.getImageUri
import com.dicoding.storyapp.reduceFileImage
import com.dicoding.storyapp.uriToFile
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainViewModel
import kotlinx.coroutines.launch

class UploadActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityUploadBinding

    private var currentImageUri: Uri? = null

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getSession().observe(this) { user ->
            token = user.token
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        showLoading(false)
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.toast.observe(this@UploadActivity) { message ->
            showToast(message)
        }
        viewModel.finish.observe(this@UploadActivity) { status ->
            if (status == true) {
                finish()
            }
        }

        binding.btnUpload.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.descriptionEditText.text.toString()
                lifecycleScope.launch {
                    viewModel.uploadImage(token, imageFile, description)
                }
            } ?: run {
                showToast("Mohon masukkan gambar.")
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgStoryUpload.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}