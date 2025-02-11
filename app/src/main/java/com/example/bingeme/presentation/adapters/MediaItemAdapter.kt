package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.BaseMediaItem

/**
 * Adapter class for displaying a list of mediaItem in a RecyclerView.
 *
 * @param baseMediaItems The list of mediaItem to display.
 * @param onItemClick Callback function to handle clicks on a media item.
 */
class MediaItemAdapter(
    private val baseMediaItems: List<BaseMediaItem>,
    private val onItemClick: (BaseMediaItem) -> Unit
) : RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder>() {

    /**
     * ViewHolder for media items in the RecyclerView.
     */
    class MediaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val poster: ImageView = view.findViewById(R.id.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return MediaItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val mediaItem = baseMediaItems[position]
        holder.title.text = mediaItem.title
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${mediaItem.posterPath}")
            .into(holder.poster)

        holder.itemView.findViewById<TextView>(R.id.date).text = mediaItem.releaseDate

        holder.itemView.setOnClickListener { onItemClick(mediaItem) }
    }

    override fun getItemCount(): Int = baseMediaItems.size
}
