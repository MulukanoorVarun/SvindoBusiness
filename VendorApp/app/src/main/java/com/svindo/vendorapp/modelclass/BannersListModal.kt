package com.svindo.vendorapp.modelclass

data class BannersListModal(
    val banner_list: List<BannerX>,
    val error: String,
    val cost: costDetails
)

data class costDetails(
    val per_view_cost:String,
    val per_click_cost:String
)