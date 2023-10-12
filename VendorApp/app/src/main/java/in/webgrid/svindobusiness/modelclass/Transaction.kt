package `in`.webgrid.svindobusiness.modelclass

data class Transaction(
    val amount: String,
    val created_datetime: String,
    val description: String,
    val id: String,
    val ref_type: String,
    val transaction_type: String,
)