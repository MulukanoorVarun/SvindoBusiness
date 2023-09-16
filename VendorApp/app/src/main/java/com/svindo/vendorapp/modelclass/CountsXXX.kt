package com.svindo.vendorapp.modelclass

data class CountsXXX(
    val delivered_order_count: String,
    val pending_order_count: String,
    val return_order_count: String,
    val store_details: StoreDetailsXX,
    val totalsales_order_count: String
)