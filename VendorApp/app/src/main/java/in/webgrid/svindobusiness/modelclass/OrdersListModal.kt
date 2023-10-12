package `in`.webgrid.svindobusiness.modelclass

data class OrdersListModal(
    var error: String,
    val orders: List<OrderX>
)