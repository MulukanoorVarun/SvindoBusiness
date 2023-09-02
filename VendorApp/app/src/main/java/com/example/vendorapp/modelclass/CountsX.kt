package com.example.vendorapp.modelclass

data class CountsX(
    val delivered_order_count: String,
    val pending_order_count: String,
    val return_order_count: String,
    val store_details: StoreDetails,
    val totalsales_order_count: String
)