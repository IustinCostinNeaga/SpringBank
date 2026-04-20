package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Currency

data class NewAccountResponse(
    val IBAN: String,
    val currency: Currency
)
