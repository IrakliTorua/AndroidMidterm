package com.example.midterm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.midterm.R
import com.example.midterm.model.Product

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvProductTitle)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val ratingTextView: TextView = itemView.findViewById(R.id.tvProductRating)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.ivProductThumbnail)

        fun bind(product: Product) {
            titleTextView.text = product.title
            priceTextView.text = "$${product.price}"
            ratingTextView.text = "★ ${product.rating}"

            Glide.with(itemView.context)
                .load(product.thumbnail)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(thumbnailImageView)

            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun removeItem(position: Int) {
        products.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}

