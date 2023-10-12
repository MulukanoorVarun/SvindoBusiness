package `in`.webgrid.svindobusiness.modelclass

data class DashboardResponseModal(
    val banners: List<BannerXXX>,
    val counts: CountsXXXX,
    val error: String,
    val orders: List<OrderXXX>
)