package com.example.bingeme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bingeme.R
import com.example.bingeme.data.models.Series

/**
 * Adapter class for displaying a list of TV series in a RecyclerView.
 *
 * @param series The list of TV series to display.
 * @param onSeriesClick Callback function to handle clicks on a series item.
 */
class SeriesAdapter(
    private val series: List<Series>,
    private val onSeriesClick: (Series) -> Unit
) : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    /**
     * ViewHolder for series items in the RecyclerView.
     */
    class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.title)
        val poster: ImageView = view.findViewById(R.id.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = series[position]
        holder.name.text = series.title

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${series.posterPath}")
            .into(holder.poster)

        holder.itemView.findViewById<TextView>(R.id.date).text = series.releaseDate


        holder.itemView.setOnClickListener { onSeriesClick(series) }
    }

    override fun getItemCount(): Int = series.size
}
