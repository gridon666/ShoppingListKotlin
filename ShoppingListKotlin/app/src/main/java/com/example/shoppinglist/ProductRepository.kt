package com.example.shoppinglist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class ProductRepository(context: Context) {
    private val helper = DatabaseHelper(context.applicationContext)

    fun getAll(): List<Product> {
        val db = helper.readableDatabase
        val result = mutableListOf<Product>()
        db.query(
            DatabaseHelper.TABLE_PRODUCTS,
            null, null, null, null, null,
            "${DatabaseHelper.COLUMN_PURCHASED} ASC, ${DatabaseHelper.COLUMN_ID} DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) result.add(cursor.toProduct())
        }
        return result
    }

    fun getById(id: Long): Product? {
        helper.readableDatabase.query(
            DatabaseHelper.TABLE_PRODUCTS, null,
            "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toProduct() else null
        }
    }

    fun insert(product: Product): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABLE_PRODUCTS, null, product.toContentValues()
        )
    }

    fun update(product: Product): Int {
        return helper.writableDatabase.update(
            DatabaseHelper.TABLE_PRODUCTS,
            product.toContentValues(),
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(product.id.toString())
        )
    }

    fun delete(id: Long): Int {
        return helper.writableDatabase.delete(
            DatabaseHelper.TABLE_PRODUCTS,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun close() = helper.close()

    private fun Product.toContentValues() = ContentValues().apply {
        put(DatabaseHelper.COLUMN_NAME, name)
        put(DatabaseHelper.COLUMN_QUANTITY, quantity)
        put(DatabaseHelper.COLUMN_PRICE, price)
        put(DatabaseHelper.COLUMN_PURCHASED, if (isPurchased) 1 else 0)
    }

    private fun Cursor.toProduct() = Product(
        id = getLong(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
        name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
        quantity = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY)),
        price = getDouble(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
        isPurchased = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PURCHASED)) == 1
    )
}
