package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var repository: ProductRepository
    private lateinit var listView: ListView
    private lateinit var emptyView: TextView
    private var products: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = ProductRepository(this)
        listView = findViewById(R.id.productsList)
        emptyView = findViewById(R.id.emptyText)

        findViewById<Button>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, AddEditProductActivity::class.java))
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, products[position].id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProducts()
    }

    private fun refreshProducts() {
        products = repository.getAll()
        emptyView.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        listView.visibility = if (products.isEmpty()) View.GONE else View.VISIBLE

        val rows = products.map { product ->
            val status = if (product.isPurchased) "✓ Куплено" else "○ Не куплено"
            val sum = String.format(Locale.getDefault(), "%.2f", product.totalPrice)
            "${product.name}\nКоличество: ${product.quantity} · Сумма: $sum ₽\n$status"
        }
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rows)
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }
}
