package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency

data class NewAccountResponse(
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
    val defaultCurrency: Currency,
)