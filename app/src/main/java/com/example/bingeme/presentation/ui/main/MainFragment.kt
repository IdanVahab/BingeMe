package com.example.bingeme.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bingeme.databinding.FragmentMainBinding
import com.example.bingeme.presentation.adapters.MediaItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Main fragment that displays a list of popular movies.
 * Users can navigate to movie details or the watchlist from this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔥 Adapter לסרטים
        val moviesAdapter = MediaItemAdapter(emptyList()) { movie ->
            val action = MainFragmentDirections.actionMainFragmentToMovieDetailsFragment(movie.id)
            findNavController().navigate(action)
        }
        binding.moviesRecyclerView.adapter = moviesAdapter
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔥 Adapter לסדרות
        val seriesAdapter = MediaItemAdapter(emptyList()) { series ->
            val action = MainFragmentDirections.actionMainFragmentToSeriesDetailsFragment(series.id)
            findNavController().navigate(action)
        }
        binding.seriesRecyclerView.adapter = seriesAdapter
        binding.seriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔥 מאזינים לרשימת הסרטים
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popularMovies.collect { movies ->
                    moviesAdapter.updateData(movies)
                }
            }
        }

        // 🔥 מאזינים לרשימת הסדרות
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popularSeries.collect { series ->
                    seriesAdapter.updateData(series)
                }
            }
        }

        // 🔹 ניהול הכפתורים
        binding.moviesButton.setOnClickListener { showMoviesList() }
        binding.seriesButton.setOnClickListener { showSeriesList() }

        // 🔥 מעבר לרשימת הצפייה
        binding.watchlistButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToWatchlistFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * 🔹 הצגת רשימת הסרטים והסתרת רשימת הסדרות
     */
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
