package com.example.bingeme.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingeme.data.models.Genre
import com.example.bingeme.databinding.ItemGenreBinding

class GenresAdapter(private val genres: List<Genre>) :
    RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            binding.genreName.text = genre.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int = genres.size
}
