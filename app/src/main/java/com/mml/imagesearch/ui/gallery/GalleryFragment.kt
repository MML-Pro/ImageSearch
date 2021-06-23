package com.mml.imagesearch.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.mml.imagesearch.R
import com.mml.imagesearch.data.Photo
import com.mml.imagesearch.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnClickListener {

    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UnsplashPhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        adapter = UnsplashPhotoAdapter(this)


        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                errorTV.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isInvisible = true
                    emptyTV.isVisible = true
                } else {
                    emptyTV.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)


        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                viewModel.searchPhotos("cats")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.retry()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photo: Photo) {
       val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

}
