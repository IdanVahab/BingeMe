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
import com.example.bingeme.data.models.Series
import com.example.bingeme.databinding.FragmentSeriesDetailsBinding
import com.example.bingeme.presentation.ui.adapters.GenresAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment responsible for displaying details about a specific series.
 * This fragment shows the series's title, overview, poster, and allows users to
 * add or remove the series from their favorites list.
 */
@AndroidEntryPoint
class SeriesDetailsFragment : Fragment() {

    private val viewModel: SeriesDetailsFragmentViewModel by viewModels()
    private var series: Series? = null
    private var _binding: FragmentSeriesDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFavorite = false
    private var isWatched = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeriesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SeriesDetailsFragmentArgs by navArgs()
        val seriesId = args.seriesId

        if (seriesId != -1) {
            viewModel.checkIfFavorite(seriesId)
            viewModel.checkIfWatched(seriesId)
            observeSeriesDetails(seriesId)
            updateFavoriteButtonState(viewModel.isFavorite.value ?: false)

        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            Log.d("SeriesDetailsFragment", "Updating button state: isFavorite = $isFavorite")
            this.isFavorite = isFavorite
            updateFavoriteButtonState(isFavorite)
        }

        viewModel.isWatched.observe(viewLifecycleOwner) { isWatched ->
            Log.d("SeriesDetailsFragment", "Updating button state: isWatched = $isWatched")
            this.isWatched = isWatched
            updateWatchedButtonState(isWatched)
        }

        binding.watchedButton.setOnClickListener {
            series?.let {
                this.isWatched = !this.isWatched
                it.isWatched = this.isWatched
                it.isFavorite = this.isFavorite
                viewModel.modifySeries(it)
                updateWatchedButtonState(it.isWatched)
            }
        }


        binding.favoriteButton.setOnClickListener {
            series?.let {
                this.isFavorite = !this.isFavorite
                it.isFavorite = this.isFavorite
                it.isWatched = this.isWatched
                viewModel.modifySeries(it)
                updateFavoriteButtonState(it.isFavorite)
            }
        }
    }

    private fun observeSeriesDetails(seriesId: Int) {
        lifecycleScope.launch {
            viewModel.getSeriesDetails(seriesId).collect { result ->
                result.onSuccess { series ->
                    series?.let {
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
        binding.seriesOverview.visibility = View.GONE
        binding.poster.visibility = View.GONE
    }

    private fun updateUI(series: Series) {
        this.series = series

        // Load the main poster image
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${series.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.poster)

        // Load the blurred background image
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${series.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.backgroundPoster)


        // Handle adult content indicator, considering null cases
        when (series.adult) {
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

        // Update series details
        binding.title.text = series.title
        binding.seriesOverview.text = series.overview
        binding.releaseYear.text = series.releaseDate
        binding.rating.text = series.voteAverage?.let { String.format("%.1f/10", it) } ?: "N/A"
        binding.popularity.text = series.popularity?.toInt()?.toString() ?: "N/A"
        binding.language.text = series.originalLanguage?.uppercase() ?: "N/A"

        // Update number of seasons and episodes
        val seasonsText = series.numberOfSeasons?.let { "$it Seasons" } ?: getString(R.string.seasons_not_available)
        val episodesText = series.numberOfEpisodes?.let { "$it Episodes" } ?: getString(R.string.episodes_not_available)
        binding.numSeasons.text = seasonsText
        binding.numEpisodes.text = episodesText

        //16:9
        val webView = binding.youtubeWebView
        val width = webView.width
        val videoHeight = (width * 9) / 16
        webView.layoutParams.height =videoHeight



        // Handle series trailer
        if(series.trailerUrl != null){
            // Load and display the series trailer in WebView
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
            binding.youtubeWebView.loadUrl(series.trailerUrl)

            // Set up RecyclerView for genres
            val genresAdapter = series.genres?.let { GenresAdapter(it) }
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

    override fun onResume() {
        super.onResume()
        series?.let {
            viewModel.checkIfFavorite(it.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}