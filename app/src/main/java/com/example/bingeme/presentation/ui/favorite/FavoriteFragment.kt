package com.example.bingeme.presentation.ui.favorite

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
import com.example.bingeme.R
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.databinding.FragmentFavoriteBinding
import com.example.bingeme.presentation.adapters.MediaItemAdapter
import com.example.bingeme.presentation.ui.main.MainFragmentDirections
import com.example.bingeme.presentation.ui.main.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteFragmentViewModel by viewModels()
    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding!!
    private lateinit var moviesAdapter: MediaItemAdapter
    private lateinit var seriesAdapter: MediaItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupButtons()
        observeViewModel()
    }

    private fun setupAdapters() {
        moviesAdapter = MediaItemAdapter(emptyList(),
            onItemClick = { mediaItem ->
                val action = MainFragmentDirections
                    .actionMainFragmentToMovieDetailsFragment(mediaItem.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { mediaItem ->
                viewModel.modifyMovie(mediaItem as Movie)
            },
            onWatchedClick = { mediaItem ->
                viewModel.modifyMovie(mediaItem as Movie)
            }
        )
        _binding.moviesRecyclerView.adapter = moviesAdapter
        _binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        seriesAdapter = MediaItemAdapter(emptyList(),
            onItemClick = { mediaItem ->
                val action = MainFragmentDirections
                    .actionMainFragmentToSeriesDetailsFragment(mediaItem.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { mediaItem ->
                viewModel.modifySeries(mediaItem as Series)
            },
            onWatchedClick = { mediaItem ->
                viewModel.modifySeries(mediaItem as Series)
            }
        )
        _binding.seriesRecyclerView.adapter = seriesAdapter
        _binding.seriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.favoriteMovies.collect { movies ->
                        moviesAdapter.updateData(movies)
                    }
                }
                launch {
                    viewModel.favoriteSeries.collect { series ->
                        println("🔄 Updating UI with ${series.size} series") // ✅ בדיקה שהתצוגה מקבלת את הנתונים
                        seriesAdapter.updateData(series)
                    }
                }
                launch {
                    viewModel.currentListType.collect { listType ->
                        when (listType) {
                            MainFragmentViewModel.ListType.MOVIES -> {
                                binding.moviesRecyclerView.visibility = View.VISIBLE
                                binding.seriesRecyclerView.visibility = View.GONE
                            }

                            MainFragmentViewModel.ListType.SERIES -> {
                                binding.moviesRecyclerView.visibility = View.GONE
                                binding.seriesRecyclerView.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupButtons() {

        binding.moviesButton.setOnClickListener {
            viewModel.setCurrentListType(MainFragmentViewModel.ListType.MOVIES)
            showMoviesList()
        }

        binding.seriesButton.setOnClickListener {
            viewModel.setCurrentListType(MainFragmentViewModel.ListType.SERIES)
            showSeriesList()
        }
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
