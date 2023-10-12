package `in`.webgrid.svindobusiness.modelclass

data class OrderAcceptModal(
    val delivery_boy_details: String,
    val error: String,
    val message: String,
    val list: List<OrderDetailsList>,
    val order_details: OrderDetails,
    val bills:String
)

data class OrderDetailsList(
    val cart_quantity: String,
    val id: String,
    val image: String,
    val name: String,
    val quantity: String,
    val sale_price: String,
    val total_price: String,
    val unit: String
)

data class OrderDetails(
    val customer_address: String,
    val id: String,
    val is_delivery_boy_available: String,
    val net_payable_amount: String,
    val order_amount: String,
    val order_date: String,
    val order_number: String,
    val order_status: String,
    val otp: String,
    val payment_type: String,
    val user_name: String,
    val porter_delivery_fee:String,
    val delivery_discount:String,
    val delivery_type:String
)