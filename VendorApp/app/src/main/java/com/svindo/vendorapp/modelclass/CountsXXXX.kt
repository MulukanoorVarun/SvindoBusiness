package com.svindo.vendorapp.modelclass

data class CountsXXXX(
    val delivered_order_count: String,
    val pending_order_count: String,
    val return_order_count: String,
    val store_details: StoreDetailsXXX,
    val totalsales_order_count: String
)