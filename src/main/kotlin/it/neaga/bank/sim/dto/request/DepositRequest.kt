package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency

data class DepositRequest(
    val iban: String,
    val amount: Double,
    val currency: Currency
)
