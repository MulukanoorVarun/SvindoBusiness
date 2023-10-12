package `in`.webgrid.svindobusiness.modelclass

data class CountsXX(
    val delivered_order_count: String,
    val pending_order_count: String,
    val return_order_count: String,
    val store_details: StoreDetailsX,
    val totalsales_order_count: String
)