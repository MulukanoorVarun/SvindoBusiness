package com.svindo.vendorapp.modelclass

data class Accounts_Response(
//    val error: String,
//    val profile: Profile,
//
//    val transaction: List<Transaction>
    val error: String,
    val profile: ProfileX,
    val transaction: List<TransactionX>
)