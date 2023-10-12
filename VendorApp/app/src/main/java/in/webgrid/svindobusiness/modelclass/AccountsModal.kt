package `in`.webgrid.svindobusiness.modelclass

data class AccountsModal(
    val details: Details,
    val error: String,
    val qr_link:String,
    val report:String,
    val shop_view_cost:String,
    val shop_click_cost:String
)