package it.neaga.bank.sim.service

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.NewAccountResponse
import org.springframework.stereotype.Service

@Service
class AccountService {

    fun createNewAccount(newAccount: NewAccountRequest): NewAccountResponse{
        TODO()
    }

}