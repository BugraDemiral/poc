package com.monomobile.poc.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monomobile.poc.databinding.SeasonItemBinding
import com.monomobile.poc.model.SeasonItem

class SeasonsAdapter(private val seasonItemList: List<SeasonItem>)
    : RecyclerView.Adapter<SeasonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsAdapter.ViewHolder {
        LayoutInflater.from(parent.context).let {
            return ViewHolder(SeasonItemBinding.inflate(it, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(seasonItemList[position])

    override fun getItemCount(): Int = seasonItemList.size

    fun getSelected() = seasonItemList.filter { it.selected }.joinToString { it.title }

    fun resetSelected() {
        seasonItemList.map { it.selected = false }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: SeasonItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(seasonItem: SeasonItem) {
            itemView.setOnClickListener {
                seasonItem.selected = !seasonItem.selected
                notifyDataSetChanged()
            }

            if (seasonItem.selected) {
                binding.filterTick.visibility = View.VISIBLE
            } else {
                binding.filterTick.visibility = View.GONE
            }

            val text = "Season ${seasonItem.title}"
            binding.filterName.text = text
            binding.executePendingBindings()
        }
    }

}

