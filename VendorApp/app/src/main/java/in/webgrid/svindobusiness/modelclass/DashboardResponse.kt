package `in`.webgrid.svindobusiness.modelclass

data class DashboardResponse(
    val banners: List<Any>,
    val counts: CountsXXX,
    val error: String,
    val orders: List<OrderXX>
)