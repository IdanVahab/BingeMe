package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bingeme.R
import com.example.bingeme.data.models.Movie

/**
 * Adapter class for displaying and managing a watchlist in a RecyclerView.
 * Uses ListAdapter to handle updates and optimize list changes.
 */
class WatchlistAdapter : ListAdapter<Movie, WatchlistAdapter.WatchlistViewHolder>(DIFF_CALLBACK) {

    private var onDeleteClickListener: ((Movie) -> Unit)? = null

    /**
     * Sets the callback function for handling delete button clicks.
     *
     * @param listener The callback function to invoke when delete is clicked.
     */
    fun setOnDeleteClickListener(listener: (Movie) -> Unit) {
        onDeleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_item, parent, false)
        return WatchlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    /**
     * ViewHolder for watchlist items in the RecyclerView.
     */
    inner class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.title)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
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
