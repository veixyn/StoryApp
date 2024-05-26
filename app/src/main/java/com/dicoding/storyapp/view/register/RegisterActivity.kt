package com.dicoding.storyapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.view.ViewModelFactory
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.isLoading.observe(this) {
                showLoading(it)
            }

            viewModel.message.observe(this@RegisterActivity) {
                showToast(it)
            }

            viewModel.success.observe(this@RegisterActivity) {
                if (it == true) {
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Yeah!")
                        setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Oops..")
                        setMessage("Mohon cek ulang kredensialnya..")
                        setPositiveButton("Lanjut") { _, _ -> }
                        create()
                        show()
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.userRegister(name, email, password)
            }
        }
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(250)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(250)
        val nameLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(250)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(250)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(250)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(250)
        val button = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(250)

        val nameTogether = AnimatorSet().apply {
            playTogether(nameLayout, nameEdit)
        }

        val emailTogether = AnimatorSet().apply {
            playTogether(emailLayout, emailEdit)
        }

        val passwordTogether = AnimatorSet().apply {
            playTogether(passwordLayout, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameTogether,
                email,
                emailTogether,
                password,
                passwordTogether,
                button
            )
            start()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}