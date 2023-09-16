package com.svindo.vendorapp.modelclass

data class SubCategoryModal(
    val error: String,
    val subcategories: List<Subcategory>,
    val sizes: List<Sizes>,
)

data class Sizes(
    val id: String,
    val size_name: String
)