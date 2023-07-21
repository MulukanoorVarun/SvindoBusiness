package com.example.vendorapp.modelclass

data class WalletModal(
    val error: String,
    val message: String,
    val transaction: List<Transaction>,
    val user_data: UserData
)