package com.mml.imagesearch.api

import com.mml.imagesearch.data.Photo

data class Response(
    val results: List<Photo>
)