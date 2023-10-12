package `in`.webgrid.svindobusiness.modelclass

data class InsightsModalClass(
    val error: String,
    val followers: String,
    val mostbuy: List<Mostbuy>,
    val shopviews: String,
    val toplike: List<Toplike>,
    val toprated: List<Toprated>
)