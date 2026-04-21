package it.neaga.bank.sim.dto.request

import it.neaga.bank.sim.model.Currency
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.bind.Name
import org.springframework.format.annotation.NumberFormat

data class NewAccountRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    val name: String,
    @field:NotBlank
    @field:Size(min = 3, max = 25, message = "Surname must be between 3 and 25 characters")
    val surname: String,
    @field:Email(message = "Must be a valid email address")
    val email: String,
    @field:NumberFormat(pattern = "[0-9,-]*")
    val phone: String,
    @field:Size(min = 10, message = "Password must be at least 10 characters")
    val password: String,
    val defaultCurrency: Currency,
)