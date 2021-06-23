package com.mml.imagesearch.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.mml.imagesearch.api.API
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor (private val api:API) {

    fun getSearchResults(query:String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {UnsplashPagingSource(api,query)}
        ).liveData

}