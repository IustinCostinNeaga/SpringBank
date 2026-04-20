package it.neaga.bank.sim.model

data class NewAccount(
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
    val defaultCurrency: Currency,
    val startingBalance: Double,
)
