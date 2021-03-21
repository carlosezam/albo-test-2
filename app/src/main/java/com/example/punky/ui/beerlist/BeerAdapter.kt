package com.example.punky.ui.beerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
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
    var id: Int,
    val title:String,
    val description: String,
    val imageUrl: String?
    )

class BeerViewHolder(private val binding: BeerViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBinding( data: BeerItem, callback: (BeerViewHolderBinding) -> Unit ){
        binding.titleText.text = data.title
        binding.descriptionText.text = data.description
        binding.mediaImage.transitionName = "image_${data.id}"

        itemView.setOnClickListener {
            callback( binding )
        }

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
    override fun areItemsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem == newItem
}

class BeerAdapter(private val callback: (BeerViewHolderBinding, BeerItem, Int) -> Unit) : PagingDataAdapter< BeerItem, BeerViewHolder >( BeerDiffCallback() ){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerViewHolderBinding.inflate( inflater, parent, false )
        return BeerViewHolder( binding )
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        getItem(position)?.let { data ->
            holder.onBinding( data ){
                callback.invoke( it, data, position)
            }
        }
    }

}