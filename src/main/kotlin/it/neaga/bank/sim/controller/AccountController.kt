package it.neaga.bank.sim.controller

import it.neaga.bank.sim.model.NewAccount
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController {

    @PostMapping("/new")
    fun newAccount(@RequestBody accountData: NewAccount){
        println(accountData)
    }

}