package com.example.bingeme.presentation.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.example.bingeme.databinding.FragmentMainBinding
import com.example.bingeme.presentation.adapters.MediaItemAdapter
import com.example.bingeme.utils.setupButtonWithAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var moviesAdapter: MediaItemAdapter
    private lateinit var seriesAdapter: MediaItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupSearchView()
        setupPagingButtons()
        observeViewModel()
    }

    private fun setupAdapters() {
        moviesAdapter = MediaItemAdapter(
            requireContext(),
            emptyList(),
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
        binding.moviesRecyclerView.adapter = moviesAdapter
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        seriesAdapter = MediaItemAdapter(
            requireContext(),
            emptyList(),
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
        binding.seriesRecyclerView.adapter = seriesAdapter
        binding.seriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let { viewModel.searchMoviesAndSeries(it.toString()) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentMovies.collect { movies ->
                        moviesAdapter.updateData(movies)
                    }
                }
                launch {
                    viewModel.currentSeries.collect { series ->
                        println("🔄 Updating UI with ${series.size} series") // ✅ בדיקה שהתצוגה מקבלת את הנתונים
                        seriesAdapter.updateData(series)
                    }
                }
                launch {
                    viewModel.currentMoviesPageFlow.collect { page ->
                        if (viewModel.currentListType.value == MainFragmentViewModel.ListType.MOVIES) {
                            binding.pageNumberText.text = getString(R.string.movie_page)+ page
                            binding.pageNumberText.visibility = View.VISIBLE
                        }
                    }
                }
                launch {
                    viewModel.currentSeriesPageFlow.collect { page ->
                        if (viewModel.currentListType.value == MainFragmentViewModel.ListType.SERIES) {
                            binding.pageNumberText.text = getString(R.string.series_page)+ page
                            binding.pageNumberText.visibility = View.VISIBLE
                        }
                    }
                }
                launch {
                    viewModel.currentListType.collect { listType ->
                        when (listType) {
                            MainFragmentViewModel.ListType.MOVIES -> {
                                binding.moviesRecyclerView.visibility = View.VISIBLE
                                binding.seriesRecyclerView.visibility = View.GONE
                                binding.searchView.text.clear()  // מחיקת הטקסט ב-EditText

                            }

                            MainFragmentViewModel.ListType.SERIES -> {
                                binding.moviesRecyclerView.visibility = View.GONE
                                binding.seriesRecyclerView.visibility = View.VISIBLE
                                binding.searchView.text.clear()  // מחיקת הטקסט ב-EditText
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setupPagingButtons() {
        binding.nextPageButton.setOnClickListener { viewModel.loadNextPage() }
        setupButtonWithAnimation(binding.nextPageButton) {
            viewModel.loadNextPage()
        }

        setupButtonWithAnimation(binding.prevPageButton) {
            viewModel.loadPreviousPage()
        }

        binding.moviesButton.setOnClickListener {
            viewModel.setCurrentListType(MainFragmentViewModel.ListType.MOVIES)
            viewModel.getPopularMovies()
            showMoviesList()
        }

        binding.seriesButton.setOnClickListener {
            viewModel.setCurrentListType(MainFragmentViewModel.ListType.SERIES)
            viewModel.getPopularSeries()
            showSeriesList()
        }

        setupButtonWithAnimation(binding.favoriteButton) {
            val action = MainFragmentDirections.actionMainFragmentToFavoriteFragment()
            findNavController().navigate(action)
        }

        setupButtonWithAnimation(binding.watchedButton) {
            val action = MainFragmentDirections.actionMainFragmentToWatchedFragment()
            findNavController().navigate(action)
        }
    }

    private fun showMoviesList() {
        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.seriesRecyclerView.visibility = View.GONE
        binding.pageNumberText.visibility = View.VISIBLE
        binding.moviesButtonCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        binding.seriesButtonCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        binding.searchView.text.clear()  // מחיקת החיפוש
        viewModel.searchMoviesAndSeries("")
    }

    private fun showSeriesList() {
        binding.seriesRecyclerView.visibility = View.VISIBLE
        binding.moviesRecyclerView.visibility = View.GONE
        binding.pageNumberText.visibility = View.VISIBLE
        binding.moviesButtonCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        binding.seriesButtonCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        binding.searchView.text.clear()  // מחיקת החיפוש
        viewModel.searchMoviesAndSeries("")
    }
}
