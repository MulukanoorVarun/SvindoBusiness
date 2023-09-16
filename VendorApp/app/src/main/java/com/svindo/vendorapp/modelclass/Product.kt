package com.svindo.vendorapp.modelclass

data class Product(
    val id: String,
    val image: String,
    val in_stock: String,
    val mrp_price: String,
    val name: String,
    val quantity: String,
    val sale_price: String,
    val unit: String,
    val available_stock_count:String
)