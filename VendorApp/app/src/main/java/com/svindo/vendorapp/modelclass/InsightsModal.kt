package com.svindo.vendorapp.modelclass

data class InsightsModal(
    val error: String,
    val followers: String,
    val mostbuy: List<Any>,
    val shopviews: String,
    val toplike: List<Any>,
    val toprated: List<Any>
)