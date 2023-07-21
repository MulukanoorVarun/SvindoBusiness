package com.example.vendorapp.modelclass

data class Order(
    val amount: String,
    val delivery_date: String,
    val id: String,
    val image_path: String,
    val order_number: String,
    val payment_type: String,
    val tot_items: String
)