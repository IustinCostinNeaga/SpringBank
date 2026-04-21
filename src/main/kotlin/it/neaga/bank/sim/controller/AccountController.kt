package it.neaga.bank.sim.controller

import it.neaga.bank.sim.dto.request.DepositRequest
import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.request.WireTransferRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.DepositResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.service.AccountService
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
@Validated
class AccountController(var accountService: AccountService) {

    @PostMapping("/new")
    fun newAccount(@Valid @RequestBody accountData: NewAccountRequest): NewAccountResponse{
        return accountService.createNewAccount(accountData)
    }

    @GetMapping("/{iban}")
    fun getAccount(
        @PathVariable
        @Size(min = 27, max = 27, message = "Iban must be 27 characters")
        iban: String
    ): Account {
        return accountService.getAccount(iban)
    }

    @GetMapping("/{iban}/balance")
    fun getBalance(
        @PathVariable
        @Size(min = 27, max = 27, message = "Iban must be 27 characters")
        iban: String,
        @RequestParam(required = false)
        currency: Currency?
    ): BalanceResponse {
        return accountService.getAccountBalance(iban, currency)
    }

    @PatchMapping("/transfer")
    fun wireTransfer(@Valid @RequestBody wireTransfer: WireTransferRequest): WireTransferResponse{
        return accountService.transfer(wireTransfer)
    }

    @PatchMapping("/deposit")
    fun deposit(@Valid @RequestBody depositRequest: DepositRequest): DepositResponse {
        return accountService.addBalance(depositRequest)
    }

}