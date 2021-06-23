package com.mml.imagesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mml.imagesearch.api.API
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val api: API,
    private val query: String,
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {

            val response = api.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX)
                    null else
                    position - 1,
                nextKey = if (photos.isEmpty()) null else position - 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }
}