package com.m391.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.m391.asteroidradar.Asteroid
import com.m391.asteroidradar.databinding.AsteroidItemBinding


class MainAdapter(private val clickListener: AsteroidListener) :
    ListAdapter<Asteroid, MainAdapter.AsteroidViewHolder>(DiffCallback) {

    class AsteroidViewHolder(var binding: AsteroidItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val withDataBinding: AsteroidItemBinding = AsteroidItemBinding.inflate(LayoutInflater.from(parent.context))
        return AsteroidViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)

        holder.also {
            it.itemView.setOnClickListener{
                clickListener.onClick(asteroid)
            }
            it.bind(asteroid)
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }
}