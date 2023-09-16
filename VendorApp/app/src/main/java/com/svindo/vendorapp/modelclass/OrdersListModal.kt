package com.svindo.vendorapp.modelclass

data class OrdersListModal(
    var error: String,
    val orders: List<OrderX>
)