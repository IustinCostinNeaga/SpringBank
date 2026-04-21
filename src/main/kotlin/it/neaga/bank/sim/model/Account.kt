package it.neaga.bank.sim.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigInteger

@Entity
@Table(name = "account")
data class Account(
    @Id
    val iban: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val defaultCurrency: Currency = Currency.EUR,
    val balance: Double = 0.0,
) {
    fun withdraw(amount: Double): Account = this.copy(balance = this.balance - amount)
    fun deposit(amount: Double): Account = this.copy(balance = this.balance + amount)
}
