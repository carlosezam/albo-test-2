package com.example.punky.ui.beerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.punky.databinding.BeerViewHolderBinding
import com.example.punky.R
import com.example.punky.utils.LoadingDrawable

data class BeerItem (
    val title:String,
    val description: String,
    val imageUrl: String?
    )
class BeerViewHolder(val binding: BeerViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBinding( data: BeerItem ){
        binding.titleText.text = data.title
        binding.descriptionText.text = data.description

        if( ! data.imageUrl.isNullOrBlank()){

            var options = RequestOptions().apply {
                placeholder( LoadingDrawable( itemView.context, R.color.black) )
                error( R.drawable.ic_error_load_image )
                //diskCacheStrategy( DiskCacheStrategy.NONE)
                //skipMemoryCache(true)
            }

            Glide.with( itemView )
                .load( data.imageUrl )
                .apply( options )
                .into( binding.mediaImage )
        }

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