package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.BaseMediaItem
import com.example.bingeme.data.models.Series

/**
 * Adapter class for displaying a list of mediaItem in a RecyclerView.
 *
 * @param baseMediaItems The list of mediaItem to display.
 * @param onItemClick Callback function to handle clicks on a media item.
 */
class MediaItemAdapter(
    private var items: List<BaseMediaItem>,
    private val onItemClick: (BaseMediaItem) -> Unit,
    private val onFavoriteClick: (BaseMediaItem) -> Unit,
    private val onWatchedClick: (BaseMediaItem) -> Unit
) : RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder>() {


    /**
     * ViewHolder for media items in the RecyclerView.
     */
    class MediaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val poster: ImageView = view.findViewById(R.id.poster)
        val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton)
        val watchedButton: ImageButton = view.findViewById(R.id.watchedButton)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return MediaItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val mediaItem = items[position]
        holder.title.text = mediaItem.title
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${mediaItem.posterPath}")
            .into(holder.poster)

        holder.itemView.findViewById<TextView>(R.id.date).text = mediaItem.releaseDate

        // Update button visuals based on movie status
        updateFavoriteButtonState(holder.favoriteButton, mediaItem.isFavorite)
        updateWatchedButtonState(holder.watchedButton, mediaItem.isWatched)

        holder.favoriteButton.setOnClickListener {
            val newFavoriteState = !mediaItem.isFavorite
            mediaItem.isFavorite = newFavoriteState
            updateFavoriteButtonState(holder.favoriteButton, mediaItem.isFavorite)
            onFavoriteClick(mediaItem)
        }

        holder.watchedButton.setOnClickListener {
            val newWatchedState = !mediaItem.isWatched
            mediaItem.isWatched = newWatchedState
            updateWatchedButtonState(holder.watchedButton, mediaItem.isWatched)
            onWatchedClick(mediaItem)
        }

        holder.itemView.setOnClickListener { onItemClick(mediaItem) }
}

    fun updateData(newItems: List<BaseMediaItem>) {
        items = newItems
        notifyDataSetChanged()  // 🔥 עדכון התצוגה
    }

    override fun getItemCount(): Int = items.size

    private fun updateFavoriteButtonState(button: ImageButton, isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.favorite_icon else R.drawable.not_favorite_icon
        button.setImageResource(icon)
    }

    private fun updateWatchedButtonState(button: ImageButton, isWatched: Boolean) {
        val icon = if (isWatched) R.drawable.watch_icon else R.drawable.not_watch_icon
        button.setImageResource(icon)
    }

}

