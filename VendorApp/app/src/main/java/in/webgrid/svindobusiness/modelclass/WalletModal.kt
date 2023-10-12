package `in`.webgrid.svindobusiness.modelclass

data class WalletModal(
    val error: String,
    val message: String,
    val transaction: List<Transaction>,
    val user_data: UserData
)