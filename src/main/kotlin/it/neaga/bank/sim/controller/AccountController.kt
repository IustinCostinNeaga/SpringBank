package it.neaga.bank.sim.controller

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.service.AccountService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(var accountService: AccountService) {

    @PostMapping("/new")
    fun newAccount(@RequestBody accountData: NewAccountRequest): NewAccountResponse{
        return accountService.createNewAccount(accountData)
    }

}