package com.svindo.vendorapp.modelclass

data class CustomerFeedbackModal(
    val `data`: List<Data>,
    val error: String,
    val message: String
)