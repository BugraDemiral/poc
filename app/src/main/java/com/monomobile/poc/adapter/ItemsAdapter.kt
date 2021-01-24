package com.monomobile.poc.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.monomobile.poc.databinding.ListItemBinding
import com.monomobile.poc.model.ArtistItem
import com.squareup.picasso.Picasso
import android.widget.Filter

class ItemsAdapter(val itemList : List<ArtistItem>,
                   val rowListener : (ArtistItem?) -> Unit) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private lateinit var binding: ListItemBinding
    private var itemFilterList : List<ArtistItem> = itemList

    var appearance : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemBinding.inflate(layoutInflater, parent, false)

        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemFilterList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if(position >= 0 && position < itemFilterList.size) {
            val item = itemFilterList[position]
            holder.bind(item)
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                itemFilterList = if (charSearch.isEmpty()) {
                    itemList
                } else {
                    itemList.filter {
                        it.name?.contains(charSearch, ignoreCase = true) == true
                    }
                }

                if(!appearance.isNullOrEmpty()) {
                    itemFilterList = itemFilterList.filter {
                        it.appearance?.joinToString() == appearance
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = itemFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemFilterList = results?.values as List<ArtistItem>
                notifyDataSetChanged()
            }

        }
    }

    inner class ItemViewHolder(private val mBinding: ListItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
        fun bind(model: ArtistItem) {

            mBinding.root.setOnClickListener {
                rowListener(model)
            }

            mBinding.itemTitle.text = model.name

            if(model.img != null) {
                Picasso.get().load(model.img).into(mBinding.itemImage)
            }

            mBinding.executePendingBindings()
        }
    }
}