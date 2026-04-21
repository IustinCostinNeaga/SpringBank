package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Currency

data class WireTransferResponse(
    val amountTransferred: Double,
    val currency: Currency
)