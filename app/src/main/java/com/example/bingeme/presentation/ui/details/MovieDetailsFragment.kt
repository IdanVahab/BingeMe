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
import android.widget.Toast
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
    private var isFavorite = false
    private var isWatched = false

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
            viewModel.checkIfFavorite(movieId)
            viewModel.checkIfWatched(movieId)
            observeMovieDetails(movieId)
            updateFavoriteButtonState(viewModel.isFavorite.value ?: false)
            updateWatchedButtonState(viewModel.isWatched.value ?: false)

        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            Log.d("MovieDetailsFragment", "Updating button state: isFavorite = $isFavorite")
            this.isFavorite = isFavorite
            updateFavoriteButtonState(isFavorite)
        }

        viewModel.isWatched.observe(viewLifecycleOwner) { isWatched ->
            Log.d("MovieDetailsFragment", "Updating button state: isWatched = $isWatched")
            this.isWatched = isWatched
            updateWatchedButtonState(isWatched)
        }

        binding.watchedButton.setOnClickListener {
            movie?.let {
                this.isWatched = !this.isWatched
                it.isWatched = this.isWatched
                it.isFavorite = this.isFavorite
                viewModel.modifyMovie(it)
                updateWatchedButtonState(it.isWatched)
                showWatchedUpdateMessage(this.isWatched,it.title)
            }
        }


        binding.favoriteButton.setOnClickListener {
            movie?.let {
                this.isFavorite = !this.isFavorite
                it.isFavorite = this.isFavorite
                it.isWatched = this.isWatched
                viewModel.modifyMovie(it)
                updateFavoriteButtonState(it.isFavorite)
                showFavoriteUpdateMessage(this.isFavorite,it.title)
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

        // Update movie details
        binding.title.text = movie.title
        binding.movieOverview.text = movie.overview
        binding.releaseYear.text = movie.releaseDate
        binding.rating.text = movie.voteAverage?.let { String.format("%.1f/10", it) } ?: "N/A"
        binding.popularity.text = movie.popularity?.toInt()?.toString() ?: "N/A"
        binding.language.text = movie.originalLanguage?.uppercase() ?: "N/A"


        //16:9
        val webView = binding.youtubeWebView
        val width = webView.width
        val videoHeight = (width * 9) / 16
        webView.layoutParams.height =videoHeight



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
        binding.favoriteButton.text = if (isFavorite) {
            getString(R.string.remove_from_favorites)
        } else {
            getString(R.string.add_to_favorites)
        }

        val icon = if (isFavorite) {
            R.drawable.favorite_icon
        } else {
            R.drawable.not_favorite_icon
        }

        binding.favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0, 0, icon, 0
        )
    }

    private fun updateWatchedButtonState(isWatched: Boolean) {
        val text = if (isWatched) {
            getString(R.string.watched)
        } else {
            getString(R.string.not_watched)
        }

        val icon = if (isWatched) {
            R.drawable.watch_icon
        } else {
            R.drawable.not_watch_icon
        }

        binding.watchedButton.text = text
        binding.watchedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
    }

    private fun showFavoriteUpdateMessage(isFavorite: Boolean, title: String) {
        val message = if (isFavorite) {
            "$title added to favorites."
        } else {
            "$title removed from favorites."
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showWatchedUpdateMessage(isWatched: Boolean, title: String) {
        val message = if (isWatched) {
            "$title added to Watched."
        } else {
            "$title removed from Watched."
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        movie?.let {
            viewModel.checkIfFavorite(it.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
