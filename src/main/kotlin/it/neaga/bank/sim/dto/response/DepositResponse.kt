package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency

data class DepositResponse(
    val amount: Double,
    val currency: Currency,
    val convertedAmount: Double? = null,
    val accountCurrency: Currency? = null,
    val rate: Double? = null,
    val accountAfterDeposit: Account
)