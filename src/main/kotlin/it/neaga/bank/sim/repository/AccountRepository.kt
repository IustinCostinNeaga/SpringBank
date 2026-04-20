package it.neaga.bank.sim.repository

import it.neaga.bank.sim.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, String> {
}