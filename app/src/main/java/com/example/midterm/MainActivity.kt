package com.example.midterm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.midterm.adapter.ProductAdapter
import com.example.midterm.model.ApiClient
import com.example.midterm.model.Product
import com.example.midterm.util.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerView()
        setupSwipeToRefresh()
        setupSwipeToDelete()

        fetchProducts()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(products) { product ->
            navigateToProductDetail(product)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            fetchProducts()
        }
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedProduct = products[position]

                productAdapter.removeItem(position)

                Snackbar.make(
                    recyclerView,
                    "${deletedProduct.title} removed from cart",
                    Snackbar.LENGTH_LONG
                ).setAction("UNDO") {
                    products.add(position, deletedProduct)
                    productAdapter.notifyItemInserted(position)
                    recyclerView.scrollToPosition(position)
                }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getProducts()
                if (response.isSuccessful) {
                    response.body()?.let { productResponse ->
                        productAdapter.updateProducts(productResponse.products)
                    }
                } else {
                    showError("Failed to fetch products: ${response.message()}")
                }
            } catch (e: IOException) {
                showError("Network error: ${e.message}")
            } catch (e: HttpException) {
                showError("HTTP error: ${e.message}")
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(ProductDetailActivity.EXTRA_PRODUCT, product)
        }
        startActivity(intent)
    }
}