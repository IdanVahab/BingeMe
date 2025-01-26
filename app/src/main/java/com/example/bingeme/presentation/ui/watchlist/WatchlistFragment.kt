package com.example.bingeme.presentation.ui.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bingeme.R
import com.example.bingeme.databinding.FragmentWatchlistBinding
import com.example.bingeme.presentation.adapters.WatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment for displaying and managing the user's watchlist.
 * Users can view their saved movies and remove items from the watchlist.
 */
@AndroidEntryPoint
class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private val viewModel: WatchlistFragmentViewModel by viewModels()
    private lateinit var binding: FragmentWatchlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWatchlistBinding.bind(view)

        // Set up the RecyclerView with the WatchlistAdapter
        val adapter = WatchlistAdapter()
        binding.watchlistRecyclerView.adapter = adapter
        binding.watchlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set the delete click listener
        adapter.setOnDeleteClickListener { movie ->
            viewModel.removeMovieFromWatchlist(movie)
        }

        // Observe the watchlist and update the adapter
        lifecycleScope.launch {
            viewModel.watchlistMovies.collect { movies ->
                adapter.submitList(movies)
            }
        }
    }
}
