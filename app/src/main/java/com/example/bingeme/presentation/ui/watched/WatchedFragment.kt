package com.example.bingeme.presentation.ui.watched

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bingeme.R
import com.example.bingeme.databinding.FragmentWatchedBinding
import com.example.bingeme.presentation.adapters.SeriesAdapter
import com.example.bingeme.presentation.adapters.WatchedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchedFragment : Fragment(R.layout.fragment_watched) {

    private val viewModel: WatchedFragmentViewModel by viewModels()
    private lateinit var binding: FragmentWatchedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWatchedBinding.bind(view)

        val moviesAdapter = WatchedAdapter()
        binding.moviesRecyclerView.adapter = moviesAdapter
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        moviesAdapter.setOnItemClickListener { movie ->
            val action = WatchedFragmentDirections.actionWatchedFragmentToMovieDetailsFragment(movie.id)
            findNavController().navigate(action)
        }

        val seriesAdapter = SeriesAdapter(emptyList()) { series ->
            val action = WatchedFragmentDirections.actionWatchedFragmentToSeriesDetailsFragment(series.id)
            findNavController().navigate(action)
        }
        binding.seriesRecyclerView.adapter = seriesAdapter
        binding.seriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.watchedMovies.collect { movies ->
                Log.d("WatchedFragment", "Fetched ${movies.size} watched movies")
                moviesAdapter.submitList(movies)
            }
        }

        lifecycleScope.launch {
            viewModel.watchedSeries.collect { series ->
                Log.d("WatchedFragment", "Fetched ${series.size} watched series")
                seriesAdapter.updateSeries(series)
            }
        }

        binding.moviesButton.setOnClickListener { showMoviesList() }
        binding.seriesButton.setOnClickListener { showSeriesList() }
    }

    private fun showMoviesList() {
        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.seriesRecyclerView.visibility = View.GONE
    }

    private fun showSeriesList() {
        binding.seriesRecyclerView.visibility = View.VISIBLE
        binding.moviesRecyclerView.visibility = View.GONE
    }
}
