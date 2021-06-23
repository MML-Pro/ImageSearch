package com.mml.imagesearch.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mml.imagesearch.R
import com.mml.imagesearch.data.Photo
import com.mml.imagesearch.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details_) {
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val photo: Photo = args.photo

            Glide.with(this@DetailsFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_baseline_error_outline_24)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        descriptionTV.isVisible = photo.description != null
                        creatorTV.isVisible = true

                        return false
                    }

                })
                .into(imageView)

            descriptionTV.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrls)

            val intent = Intent(Intent.ACTION_VIEW, uri)

            creatorTV.apply {
                text = "Photo by ${photo.user.name} on Unsplach"
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }

        }

    }
}