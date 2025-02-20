package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.Movie

class WatchedAdapter : ListAdapter<Movie, WatchedAdapter.WatchedViewHolder>(DIFF_CALLBACK) {

    private var onDeleteClickListener: ((Movie) -> Unit)? = null
    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Movie) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_item, parent, false)
        return WatchedViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchedViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(movie)
        }
    }

    inner class WatchedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.title)
        private val releaseDate: TextView = itemView.findViewById(R.id.date)
        private val poster: ImageView = itemView.findViewById(R.id.poster)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            releaseDate.text = "Release Date: ${movie.releaseDate}"

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(poster)

            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(movie)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}
