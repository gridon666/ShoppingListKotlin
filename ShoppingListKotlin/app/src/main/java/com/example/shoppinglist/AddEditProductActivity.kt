package com.example.shoppinglist

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class AddEditProductActivity : Activity() {
    private lateinit var repository: ProductRepository
    private var productId: Long = -1L

    private lateinit var nameInput: EditText
    private lateinit var quantityInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var purchasedCheck: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        repository = ProductRepository(this)
        nameInput = findViewById(R.id.nameInput)
        quantityInput = findViewById(R.id.quantityInput)
        priceInput = findViewById(R.id.priceInput)
        purchasedCheck = findViewById(R.id.purchasedCheck)

        productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1L)
        if (productId != -1L) {
            title = "Редактирование товара"
            loadProduct()
        } else {
            title = "Добавление товара"
            quantityInput.setText("1")
            priceInput.setText("0")
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener { saveProduct() }
    }

    private fun loadProduct() {
        val product = repository.getById(productId)
        if (product == null) {
            Toast.makeText(this, "Товар не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        nameInput.setText(product.name)
        quantityInput.setText(product.quantity.toString())
        priceInput.setText(product.price.toString())
        purchasedCheck.isChecked = product.isPurchased
    }

    private fun saveProduct() {
        val name = nameInput.text.toString().trim()
        val quantity = quantityInput.text.toString().toIntOrNull()
        val price = priceInput.text.toString().trim().replace(',', '.').toDoubleOrNull()

        if (name.isBlank()) {
            nameInput.error = "Введите название товара"
            nameInput.requestFocus()
            return
        }
        if (quantity == null || quantity <= 0) {
            quantityInput.error = "Количество должно быть больше нуля"
            quantityInput.requestFocus()
            return
        }
        if (price == null || price < 0.0) {
            priceInput.error = "Введите корректную цену (не меньше нуля)"
            priceInput.requestFocus()
            return
        }

        val product = Product(
            id = if (productId == -1L) 0L else productId,
            name = name,
            quantity = quantity,
            price = price,
            isPurchased = purchasedCheck.isChecked
        )

        val success = if (productId == -1L) {
            repository.insert(product) != -1L
        } else {
            repository.update(product) > 0
        }

        if (success) {
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Не удалось сохранить данные", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "product_id"
    }
}
