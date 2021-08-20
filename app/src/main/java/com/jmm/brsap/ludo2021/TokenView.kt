package com.jmm.brsap.ludo2021

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.jmm.brsap.ludo2021.databinding.LayoutTokenViewBinding
import pl.droidsonroids.gif.GifDrawable

class TokenView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutTokenViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        loadAttributes(attrs, defStyleAttr)
    }

    private fun loadAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.TokenView,
            defStyleAttr,
            0
        )

        val tokenImage = attr.getDrawable(R.styleable.TokenView_android_src)
        val bgSpinner = attr.getInteger(R.styleable.TokenView_tvGif, 0)
        attr.recycle()

        binding.ivToken.setImageDrawable(tokenImage)

        when (bgSpinner) {
            0 -> {
                val gifDrawable = GifDrawable(resources, R.drawable.yellow_spinner)
                binding.ivSpinner.background = gifDrawable
            }
            1 -> {
                val gifDrawable = GifDrawable(resources, R.drawable.green_spinner)
                binding.ivSpinner.background = gifDrawable
            }
            2 -> {
                val gifDrawable = GifDrawable(resources, R.drawable.red_spinner)
                binding.ivSpinner.background = gifDrawable
            }
            3 -> {
                val gifDrawable = GifDrawable(resources, R.drawable.blue_spinner)
                binding.ivSpinner.background = gifDrawable
            }
        }



    }

    fun isMovable(flag: Boolean) {
        binding.ivSpinner.isVisible = flag
    }


}