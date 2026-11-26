package com.example.shoppinglist

data class Product(
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    val price: Double,
    val isPurchased: Boolean
) {
    val totalPrice: Double
        get() = quantity * price
}
