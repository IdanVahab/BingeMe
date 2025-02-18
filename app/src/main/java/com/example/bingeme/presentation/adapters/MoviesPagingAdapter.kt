package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bingeme.data.models.Movie
import com.example.bingeme.databinding.MediaItemBinding

class MoviesPagingAdapter(
    private val onClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviesPagingAdapter.MovieViewHolder>(MovieDiffCallback) {

    class MovieViewHolder(private val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, position: Int, onClick: (Movie) -> Unit) {
            binding.title.text = "$position. ${movie.title}"  // הוספת מספר לסרט
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.poster)
            binding.root.setOnClickListener { onClick(movie) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            val displayNumber = position + 1  // מספור 1-100
            holder.bind(movie, displayNumber, onClick)
        }
    }


    companion object {
        private val MovieDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }


}
