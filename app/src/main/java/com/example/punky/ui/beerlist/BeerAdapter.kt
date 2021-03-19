package com.example.punky.ui.beerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.punky.databinding.BeerViewHolderBinding


data class BeerItem (
    val title:String,
    val description: String,
    val imageUrl: String?
    )
class BeerViewHolder(val binding: BeerViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBinding( data: BeerItem ){
        binding.titleText.text = data.title
    }
}

class BeerDiffCallback : DiffUtil.ItemCallback<BeerItem>() {
    override fun areItemsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem === newItem
    override fun areContentsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem == newItem
}

class BeerAdapter : ListAdapter< BeerItem, BeerViewHolder >( BeerDiffCallback() ){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerViewHolderBinding.inflate( inflater, parent, false )
        return BeerViewHolder( binding )
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
       holder.onBinding( getItem(position) )
    }

}