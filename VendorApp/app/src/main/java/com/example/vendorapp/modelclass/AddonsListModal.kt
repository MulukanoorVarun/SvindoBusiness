package com.example.vendorapp.modelclass

data class AddonsListModal(
    val add_on_list: List<AddOn>,
    val error: String,
    val message: String
)