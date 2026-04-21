package it.neaga.bank.sim.dto.response

import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency

data class DepositResponse(
    val amount: Double,
    val currency: Currency,
    val rate: Double,
    val accountAfterDeposit: Account
)