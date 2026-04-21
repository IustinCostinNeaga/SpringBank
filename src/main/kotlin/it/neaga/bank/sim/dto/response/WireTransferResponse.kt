package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency

data class WireTransferResponse(
    val amountSent: Double,
    val currencySent: Currency,
    val amountArrived: Double,
    val currencyArrived: Currency,
    val accountAfterTransfer: Account,
)