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
import com.example.bingeme.presentation.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

        // Set up the RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the StateFlow for popular movies
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popularMovies.collect { movies ->
                    binding.recyclerView.adapter = MoviesAdapter(movies) { movie ->
                        val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(movie.id)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        // Set up navigation to the watchlist
        binding.watchlistButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToWatchlistFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
