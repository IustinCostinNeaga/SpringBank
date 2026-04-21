package it.neaga.bank.sim.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.HttpClientErrorException
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.CONFLICT)
class AccountAlreadyExistsException : RuntimeException("Account already exists")

@ResponseStatus(HttpStatus.NOT_FOUND)
class AccountNotFoundException : RuntimeException("Account not found")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NegativeDepositException : RuntimeException("You can't deposit a negative amount")

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotEnoughMoneyException : RuntimeException("You don't have enough money for the transfer")