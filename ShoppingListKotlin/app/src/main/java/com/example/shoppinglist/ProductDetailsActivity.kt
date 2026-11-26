package com.example.shoppinglist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class ProductDetailsActivity : Activity() {
    private lateinit var repository: ProductRepository
    private var productId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        repository = ProductRepository(this)
        productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1L)

        findViewById<Button>(R.id.editButton).setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            intent.putExtra(AddEditProductActivity.EXTRA_PRODUCT_ID, productId)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Удалить товар?")
                .setMessage("Запись будет удалена из списка покупок.")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Удалить") { _, _ ->
                    repository.delete(productId)
                    Toast.makeText(this, "Товар удалён", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        val product = repository.getById(productId)
        if (product == null) {
            Toast.makeText(this, "Товар не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.detailName).text = product.name
        findViewById<TextView>(R.id.detailQuantity).text = "Количество: ${product.quantity}"
        findViewById<TextView>(R.id.detailPrice).text =
            "Цена за единицу: ${String.format(Locale.getDefault(), "%.2f", product.price)} ₽"
        findViewById<TextView>(R.id.detailTotal).text =
            "Общая стоимость: ${String.format(Locale.getDefault(), "%.2f", product.totalPrice)} ₽"
        findViewById<TextView>(R.id.detailStatus).text =
            if (product.isPurchased) "Статус: куплено" else "Статус: не куплено"
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "product_id"
    }
}
