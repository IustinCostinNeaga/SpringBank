package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.NumberFormat

data class DepositRequest(
    @field:Size(min = 27, max = 27, message = "Iban must be 27 characters")
    val iban: String,
    @field:PositiveOrZero(message = "Amount must be positive")
    val amount: Double,
    val currency: Currency
)
