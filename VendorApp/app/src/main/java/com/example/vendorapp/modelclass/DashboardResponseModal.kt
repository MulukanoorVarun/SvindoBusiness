package com.example.vendorapp.modelclass

data class DashboardResponseModal(
    val banners: List<Banner>,
    val counts: Counts,
    val error: String,
    val orders: List<Order>
)