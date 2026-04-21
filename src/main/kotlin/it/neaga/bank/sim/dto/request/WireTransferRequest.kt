package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency

data class WireTransferRequest(
    val from: String,
    val to: String,
    val amount: Double,
)
