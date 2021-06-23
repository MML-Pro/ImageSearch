package com.mml.imagesearch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val width: Long,
    val height: Long,
    val color: String,

    val blurHash: String,

    val likes: Long,


    val likedByUser: Boolean,

    val description: String?,
    val urls: Urls,
    val user: User,
    val links: PhotoLinks
) : Parcelable {

    @Parcelize
    data class PhotoLinks(
        val self: String,
        val html: String,
        val download: String
    ) : Parcelable

    @Parcelize
    data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable


    @Parcelize
    data class User(
        val id: String,
        val username: String,
        val name: String,
        val links: UserLinks
    ) : Parcelable {
        val attributionUrls get() = "https://unsplash.com/$username?utm_source=ImageSearch&utm_medium=referral"
    }


    @Parcelize
    data class UserLinks(
        val self: String,
        val html: String,
        val photos: String,
        val likes: String
    ) : Parcelable
}
