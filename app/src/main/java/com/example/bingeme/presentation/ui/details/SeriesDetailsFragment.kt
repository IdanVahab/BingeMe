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
import com.example.bingeme.data.models.Series
import com.example.bingeme.databinding.FragmentSeriesDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment responsible for displaying details about a specific series.
 * This fragment shows the series title, overview, poster, and allows users to
 * add or remove the series from their favorites list.
 */
@AndroidEntryPoint
class SeriesDetailsFragment : Fragment() {

    private val viewModel: SeriesDetailsFragmentViewModel by viewModels()
    private var series: Series? = null
    private var _binding: FragmentSeriesDetailsBinding? = null
    private val binding get() = _binding!!

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
            observeSeriesDetails(seriesId)
        }

        binding.favoriteButton.setOnClickListener {
            series?.let {
                it.isFavorite = !it.isFavorite
                viewModel.toggleFavorite(it)
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
        binding.seriesOverview.visibility = View.GONE
        binding.poster.visibility = View.GONE
    }

    private fun updateUI(series: Series) {
        this.series = series
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${series.posterPath}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.poster)
        binding.title.text = series.title
        binding.seriesOverview.text = series.overview
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
