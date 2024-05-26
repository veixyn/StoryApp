package com.dicoding.storyapp.view.customUI

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.storyapp.R

class PasswordEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int,
    ) {
        if (text.toString().length < 8) {
            setError(context.getString(R.string.password_error), null)
        } else {
            error = null
        }
    }

}