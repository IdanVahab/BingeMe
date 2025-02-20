package com.example.bingeme.presentation.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.Movie
import com.example.bingeme.databinding.FragmentMovieDetailsBinding
import com.example.bingeme.presentation.ui.adapters.GenresAdapter
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
            viewModel.checkIfFavorite(movieId) // בודק האם הסרט כבר במועדפים
            observeMovieDetails(movieId)
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            Log.d("MovieDetailsFragment", "Updating button state: isFavorite = $isFavorite")
            updateFavoriteButtonState(isFavorite)
        }

        binding.watchedButton.setOnClickListener {
            movie?.let {
                viewModel.toggleWatched(it.id)
            }
        }


        binding.favoriteButton.setOnClickListener {
            movie?.let {
                viewModel.toggleFavorite(it)
                viewModel.checkIfFavorite(it.id)
            }
        }
    }

    private fun observeMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            viewModel.getMovieDetails(movieId).collect { result ->
                result.onSuccess { movie ->
                    movie?.let {
                        updateUI(it)
                    }
                }.onFailure {
                    showError()
                }
            }
        }
    }

//    private fun updateFavoriteButtonState(isFavorite: Boolean) {
//        // ✅ נוודא שהכפתור לא מקבל עדכון כפול
//        if (binding.favoriteButton.text == (if (isFavorite) "Remove from Favorites" else "Add to Favorites")) {
//            return // ❌ אל תעדכן אם הערך כבר נכון!
//        }
//
//        Log.d("MovieDetailsFragment", "Updating UI Correctly: isFavorite = $isFavorite")
//
//        requireActivity().runOnUiThread {
//            binding.favoriteButton.text = if (isFavorite) {
//                "Remove from Favorites"
//            } else {
//                "Add to Favorites"
//            }
//        }
//    }

    private fun showError() {
        binding.errorTextView.visibility = View.VISIBLE
        binding.title.visibility = View.GONE
        binding.movieOverview.visibility = View.GONE
        binding.poster.visibility = View.GONE
    }

    private fun updateUI(movie: Movie) {
        this.movie = movie

        // Load the main poster image
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.poster)

        // Load the blurred background image
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.backgroundPoster)

        // Update movie runtime, handling null values and proper formatting
        val runtimeText = when {
            movie.runtime == null -> getString(R.string.runtime_not_available)
            movie.runtime < 60 -> "${movie.runtime} min"
            else -> {
                val hours = movie.runtime / 60
                val minutes = movie.runtime % 60
                "${hours}h ${minutes}m"
            }
        }
        binding.runtime.text = runtimeText

        // Handle adult content indicator, considering null cases
        when (movie.adult) {
            false -> {
                binding.adultIcon.setImageResource(R.drawable.not_adult_icon)
                binding.adultIcon.contentDescription = getString(R.string.family_friendly)}
            null -> {
                binding.adultIcon.setImageResource(R.drawable.question_mark)
                binding.adultIcon.contentDescription = getString(R.string.unknown_age_restriction)            }
            true ->{
                binding.adultIcon.setImageResource(R.drawable.adult_icon)
                binding.adultIcon.contentDescription = getString(R.string.adult_content)
            }
        }

        // Handle favorite button state update, changing text and icon dynamically
        updateFavoriteButtonState(movie.isFavorite)

        // Handle watch button state update, changing text and icon dynamically
        updateWatchedButtonState(movie.isWatched)

        // Update movie details
        binding.title.text = movie.title
        binding.movieOverview.text = movie.overview
        binding.releaseYear.text = movie.releaseDate
        binding.rating.text = movie.voteAverage?.let { String.format("%.1f/10", it) } ?: "N/A"
        binding.popularity.text = movie.popularity?.toInt()?.toString() ?: "N/A"
        binding.language.text = movie.originalLanguage?.uppercase() ?: "N/A"

        // Handle movie trailer
        if(movie.trailerUrl != null){
            // Load and display the movie trailer in WebView
            val webSettings: WebSettings = binding.youtubeWebView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            binding.youtubeWebView.webViewClient = object : WebViewClient()
            {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    view?.loadUrl(request?.url.toString()) // Load all URLs inside WebView
                    return true
                }
            }
            binding.youtubeWebView.loadUrl(movie.trailerUrl)

            // Set up RecyclerView for genres
            val genresAdapter = movie.genres?.let { GenresAdapter(it) }
            binding.genresRecyclerView.apply {
                adapter = genresAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

        }
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteButton.text = "Saved"
            binding.favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.favorite_icon, 0 // Change icon to indicate saved state
            )
        } else {
            binding.favoriteButton.text = "Save"
            binding.favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.not_favorite_icon, 0 // Default "not saved" icon
            )
        }
    }

    private fun updateWatchedButtonState(isWatched: Boolean) {
        if (isWatched) {
            binding.watchedButton.text = "Watched"
            binding.watchedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.watch_icon, 0 // Change icon to indicate saved state
            )
        } else {
            binding.watchedButton.text = "Not Watched"
            binding.watchedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.not_watch_icon, 0 // Default "not saved" icon
            )
        }
    }

    override fun onResume() {
        super.onResume()
        movie?.let {
            viewModel.checkIfFavorite(it.id) // ✅ טוען מחדש את הסטטוס של הסרט במועדפים
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
