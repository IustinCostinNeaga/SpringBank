package it.neaga.bank.sim.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "account")
data class Account(
    @Id
    val iban: String = "",
)
