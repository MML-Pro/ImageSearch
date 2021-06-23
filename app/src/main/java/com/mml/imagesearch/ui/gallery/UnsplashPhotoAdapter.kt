package com.mml.imagesearch.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mml.imagesearch.R
import com.mml.imagesearch.data.Photo
import com.mml.imagesearch.databinding.ItemUnsplahPhotoBinding

class UnsplashPhotoAdapter(private val listener: OnClickListener) : PagingDataAdapter<Photo,
        UnsplashPhotoAdapter.PhotoViewHolder>(
    PHOTO_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemUnsplahPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    inner class PhotoViewHolder(private val binding: ItemUnsplahPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: Photo) {
            binding.apply {
                Glide.with(itemView).load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(image)

                userNameTV.text = photo.user.username
            }
        }

    }

    interface OnClickListener {
        fun onItemClick(photo: Photo)
    }


    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }

    }
}