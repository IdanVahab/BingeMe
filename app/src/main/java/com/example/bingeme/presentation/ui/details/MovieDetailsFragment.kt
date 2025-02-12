package com.example.bingeme.presentation.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.Movie
import com.example.bingeme.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment responsible for displaying details about a specific movie.
 * This fragment shows the movie's title, overview, poster, and allows users to
 * add or remove the movie from their favorites list.
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsFragmentViewModel by viewModels()
    private var movie: Movie? = null
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MovieDetailsFragmentArgs by navArgs()
        val movieId = args.movieId

        if (movieId != -1) {
            observeMovieDetails(movieId)
        }

        binding.favoriteButton.setOnClickListener {
            movie?.let {
                it.isFavorite = !it.isFavorite
                viewModel.toggleFavorite(it)
                updateFavoriteButtonState(it.isFavorite)
            }
        }
    }

    private fun observeMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            viewModel.getMovieDetails(movieId).collect { result ->
                result.onSuccess { movie ->
                    movie?.let {
                        updateUI(it)
                        updateFavoriteButtonState(it.isFavorite)
                    }
                }.onFailure {
                    showError()
                }
            }
        }
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        binding.favoriteButton.text = if (isFavorite) {
            "Remove from Favorites"
        } else {
            "Add to Favorites"
        }
    }

    private fun showError() {
        binding.errorTextView.visibility = View.VISIBLE
        binding.title.visibility = View.GONE
        binding.movieOverview.visibility = View.GONE
        binding.poster.visibility = View.GONE
    }

    private fun updateUI(movie: Movie) {
        this.movie = movie
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.poster)
        binding.title.text = movie.title
        binding.movieOverview.text = movie.overview
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
