package com.example.bingeme.presentation.ui.watchlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bingeme.R
import com.example.bingeme.databinding.FragmentWatchlistBinding
import com.example.bingeme.presentation.adapters.SeriesAdapter
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

        // 🔥 Adapter לסרטים
        val moviesAdapter = WatchlistAdapter()
        binding.moviesRecyclerView.adapter = moviesAdapter
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔥 Adapter לסדרות
        val seriesAdapter = SeriesAdapter(emptyList()) { series ->
            // אפשר להוסיף פעולה ללחיצה על סדרה כאן
        }
        binding.seriesRecyclerView.adapter = seriesAdapter
        binding.seriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔥 מאזינים לרשימת הסרטים
        lifecycleScope.launch {
            viewModel.watchlistMovies.collect { movies ->
                Log.d("WatchlistFragment", "Fetched ${movies.size} movies from DB")
                moviesAdapter.submitList(movies)
            }
        }

        // 🔥 מאזינים לרשימת הסדרות
        lifecycleScope.launch {
            viewModel.watchlistSeries.collect { series ->
                Log.d("WatchlistFragment", "Fetched ${series.size} series from DB")
                seriesAdapter.updateSeries(series) // עדכון ה-Adapter לסדרות
            }
        }

        binding.moviesButton.setOnClickListener { showMoviesList() }
        binding.seriesButton.setOnClickListener { showSeriesList() }
    }
    private fun showMoviesList() {
        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.seriesRecyclerView.visibility = View.GONE
    }

    /**
     * 🔹 הצגת רשימת הסדרות והסתרת רשימת הסרטים
     */
    private fun showSeriesList() {
        binding.seriesRecyclerView.visibility = View.VISIBLE
        binding.moviesRecyclerView.visibility = View.GONE
    }

}
