package `in`.webgrid.svindobusiness.modelclass

data class Mobileotp_Response(
    val error: Int,
    val message: String,
    val otp: Int
)