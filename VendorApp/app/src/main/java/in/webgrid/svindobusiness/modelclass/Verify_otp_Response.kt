package `in`.webgrid.svindobusiness.modelclass

data class Verify_otp_Response(
    val error: String,
    val message: String,
    val token: String
)