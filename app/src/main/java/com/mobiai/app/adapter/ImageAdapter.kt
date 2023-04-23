package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiai.R
import com.mobiai.app.model.Image

class ImageAdapter(private val context: Context, val listImage: ArrayList<Image>,val listener : OnItemImageClicked) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    fun setItems(items: List<Image>) {
        this.listImage.clear()
        this.listImage.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(listImage[position].path).into(holder.imageView)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(listImage[position].path)
        }
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    interface OnItemImageClicked{
        fun onItemClicked(image : String)
    }
}