package com.example.midterm

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.midterm.model.Product

class ProductDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PRODUCT = "extra_product"
    }

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        product = intent.getParcelableExtra(EXTRA_PRODUCT) ?: run {
            finish()
            return
        }

        supportActionBar?.apply {
            title = product.title
            setDisplayHomeAsUpEnabled(true)
        }

        setupViews()
    }

    private fun setupViews() {
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvBrand: TextView = findViewById(R.id.tvBrand)
        val tvPrice: TextView = findViewById(R.id.tvPrice)
        val tvDiscount: TextView = findViewById(R.id.tvDiscount)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvRating: TextView = findViewById(R.id.tvRating)
        val tvStock: TextView = findViewById(R.id.tvStock)
        val tvCategory: TextView = findViewById(R.id.tvCategory)
        val ivThumbnail: ImageView = findViewById(R.id.ivThumbnail)

        tvTitle.text = product.title
        tvBrand.text = product.brand ?: "Unknown Brand"
        tvPrice.text = "$${product.price}"
        tvDiscount.text = "${product.discountPercentage}% OFF"
        tvDescription.text = product.description
        tvRating.text = "Rating: ${product.rating}/5"
        tvStock.text = "In stock: ${product.stock}"
        tvCategory.text = "Category: ${product.category}"

        Glide.with(this)
            .load(product.thumbnail)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(ivThumbnail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}