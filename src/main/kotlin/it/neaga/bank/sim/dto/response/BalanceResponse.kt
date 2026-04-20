package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Currency

data class BalanceResponse (
    val balance: Double,
    val iban: String,
    val currency: Currency,
)
