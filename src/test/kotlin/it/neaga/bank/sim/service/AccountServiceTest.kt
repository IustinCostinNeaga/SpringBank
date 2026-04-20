package it.neaga.bank.sim.service

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest
class AccountServiceTest(@Autowired val accountService: AccountService) {

    @Test
    @DisplayName("should create a new account")
    fun accountCreationTest(){

        val result = accountService.createNewAccount(newAccountRequest())
        assertThat(result).isEqualTo(newAccountResponse())

    }


}