package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class WireTransferRequest(
    @field:Size(min = 27, max = 27, message = "Iban must be 27 characters")
    val from: String,
    @field:Size(min = 27, max = 27, message = "Iban must be 27 characters")
    val to: String,
    @field:PositiveOrZero(message = "Amount transferred must be positive")
    val amount: Double,
)
