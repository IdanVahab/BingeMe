package com.example.bingeme.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private val context: Context,
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
            showFavoriteUpdateMessage(newFavoriteState,mediaItem.title)
            onFavoriteClick(mediaItem)
        }

        holder.watchedButton.setOnClickListener {
            val newWatchedState = !mediaItem.isWatched
            mediaItem.isWatched = newWatchedState
            updateWatchedButtonState(holder.watchedButton, mediaItem.isWatched)
            onWatchedClick(mediaItem)
            showWatchedUpdateMessage(newWatchedState,mediaItem.title)

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

    private fun showFavoriteUpdateMessage(isFavorite: Boolean, title: String) {
        val messageResId = if (isFavorite) {
            R.string.added_to_favorites
        } else {
            R.string.removed_from_favorites
        }
        val message = context.getString(messageResId, title)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showWatchedUpdateMessage(isWatched: Boolean, title: String) {
        val messageResId = if (isWatched) {
            R.string.added_to_watched
        } else {
            R.string.removed_from_watched
        }
        val message = context.getString(messageResId, title)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }




}

