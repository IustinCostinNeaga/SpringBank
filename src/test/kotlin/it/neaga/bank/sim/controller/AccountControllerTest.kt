package it.neaga.bank.sim.controller

import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.factories.AccountFactories.account
import it.neaga.bank.sim.factories.AccountFactories.newAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.service.AccountService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class AccountControllerTest(@Autowired var accountController: AccountController, @Autowired private val webClient: RestTestClient) {

    @MockitoBean
    lateinit var accountService: AccountService

    @Test
    @DisplayName("should load AccountController")
    fun loadAccountController() {
        assertThat(accountController).isNotNull
    }

    @Test
    @DisplayName("should create a new account correctlly")
    fun accountCreationTest(){

        whenever(accountService.createNewAccount(any())).thenReturn(newAccountResponse())

        webClient.post()
            .uri("/account/new")
            .body(newAccountRequest())
            .exchange()
            .also { response -> response.expectBody<NewAccountResponse>().isEqualTo(newAccountResponse()) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).createNewAccount(newAccountRequest())
    }

    @Test
    @DisplayName("should get a new account correctly")
    fun getAccountTest(){

        val fakeIban = "IT94M0300203280778859775156"
        whenever(accountService.getAccount(any())).thenReturn(account(iban = fakeIban))

        webClient.get()
            .uri("/account/$fakeIban")
            .exchange()
            .also { response -> response.expectBody<Account>().isEqualTo(account(iban = fakeIban)) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).getAccount(fakeIban)
    }
}