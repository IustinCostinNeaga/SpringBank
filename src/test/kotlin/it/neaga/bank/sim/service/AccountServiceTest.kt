package it.neaga.bank.sim.service

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.factories.AccountFactories
import it.neaga.bank.sim.factories.AccountFactories.account
import it.neaga.bank.sim.factories.AccountFactories.newAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
class AccountServiceTest(@Autowired val accountService: AccountService) {

    @MockitoBean
    private lateinit var ibanGenerator: IbanGenerator

    @MockitoBean
    private lateinit var accountRepository: AccountRepository


    @Test
    @DisplayName("should create a new account")
    fun accountCreationTest(){

        val fakeIban = "IT95V0300203280975296921156"
        whenever(ibanGenerator.generateItIban()).thenReturn(fakeIban)
        whenever(accountRepository.save(any<Account>())).thenReturn(account())

        val result = accountService.createNewAccount(newAccountRequest())
        assertThat(result).isEqualTo(newAccountResponse(iban = fakeIban))

        verify(ibanGenerator).generateItIban()
        verify(accountRepository).save(account())

    }

    @Test
    @DisplayName("should get a new account")
    fun getAccountTest(){

        val fakeIban = "IT95V0300203280975296921156"
        whenever(accountRepository.getReferenceById(any())).thenReturn(account())

        val result = accountService.getAccount(fakeIban)
        assertThat(result).isEqualTo(account(iban = fakeIban))

        verify(accountRepository).getReferenceById(fakeIban)

    }


}