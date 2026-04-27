package it.neaga.bank.sim.controller

import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.DepositResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.exceptions.AccountAlreadyExistsException
import it.neaga.bank.sim.exceptions.AccountNotFoundException
import it.neaga.bank.sim.exceptions.NotEnoughMoneyException
import it.neaga.bank.sim.factories.AccountFactories.account
import it.neaga.bank.sim.factories.AccountFactories.balance
import it.neaga.bank.sim.factories.AccountFactories.deposit
import it.neaga.bank.sim.factories.AccountFactories.depositResponse
import it.neaga.bank.sim.factories.AccountFactories.newAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountResponse
import it.neaga.bank.sim.factories.AccountFactories.transferredResponse
import it.neaga.bank.sim.factories.AccountFactories.wireTransfer
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.service.AccountService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import javax.management.openmbean.KeyAlreadyExistsException

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class AccountControllerTest(@Autowired private val webClient: RestTestClient) {

    @MockitoBean
    lateinit var accountService: AccountService

    @Test
    @DisplayName("should create a new account correctlly")
    fun accountCreationTest() {

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
    @DisplayName("should return 4xx if body is not correct")
    fun accountCreationErrorParsingTest() {
        whenever(accountService.createNewAccount(any())).thenReturn(newAccountResponse())

        webClient.post()
            .uri("/account/new")
            .body( "\"name\": \"mario\"")
            .exchange()
            .also { response -> response.expectStatus().is4xxClientError }

        verify(accountService, never()).createNewAccount(newAccountRequest())
    }

    @Test
    @DisplayName("should return 4xx if account already exists")
    fun accountCreationErrorConflictTest() {
        whenever(accountService.createNewAccount(any())).thenThrow(AccountAlreadyExistsException::class.java)

        webClient.post()
            .uri("/account/new")
            .body( newAccountRequest())
            .exchange()
            .also { response -> response.expectStatus().is4xxClientError }

        verify(accountService).createNewAccount(newAccountRequest())
    }

    @Test
    @DisplayName("should get a account correctly")
    fun getAccountTest() {

        val fakeIban = "IT11U0300203280797111426236"
        whenever(accountService.getAccount(any())).thenReturn(account(iban = fakeIban))

        webClient.get()
            .uri("/account/$fakeIban")
            .exchange()
            .also { response -> response.expectBody<Account>().isEqualTo(account(iban = fakeIban)) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).getAccount(fakeIban)
    }

    @Test
    @DisplayName("should get a error if iban is not 27 characters")
    fun wrongIbanTest() {

        val fakeIban = "IT94M0300203286"

        webClient.get()
            .uri("/account/$fakeIban")
            .exchange()
            .also { response -> response.expectStatus().is5xxServerError }

    }

    @Test
    @DisplayName("should get 404 if an account does not exist (account check)")
    fun notFoundAccountTest() {
        val fakeIban = "IT11U0300203280797111426236"
        whenever(accountService.getAccount(any())).thenThrow(AccountNotFoundException::class.java)

        webClient.get()
            .uri("/account/$fakeIban")
            .exchange()
            .also { response -> response.expectStatus().isNotFound }

        verify(accountService).getAccount(fakeIban)
    }

    @Test
    @DisplayName("should get a account balance correctly")
    fun getAccountBalanceTest() {

        val fakeIban = "IT11U0300203280797111426236"
        whenever(accountService.getAccountBalance(any(), anyOrNull())).thenReturn(balance())

        webClient.get()
            .uri("/account/$fakeIban/balance")
            .exchange()
            .also { response -> response.expectBody<BalanceResponse>().isEqualTo(balance()) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).getAccountBalance(fakeIban, null)
    }

    @Test
    @DisplayName("should get a account balance correctly in another currency")
    fun getAccountBalanceWithDifferentCurrencyTest() {

        val fakeIban = "IT11U0300203280797111426236"
        whenever(accountService.getAccountBalance(any(), anyOrNull())).thenReturn(balance(currency = Currency.USD))

        webClient.get()
            .uri("/account/$fakeIban/balance?currency=${Currency.USD.name}")
            .exchange()
            .also { response -> response.expectBody<BalanceResponse>().isEqualTo(balance(currency = Currency.USD)) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).getAccountBalance(fakeIban, Currency.USD)
    }

    @Test
    @DisplayName("should get a error if iban for balance is not 27 characters")
    fun wrongIbanInBalanceTest() {

        val fakeIban = "IT94M030020328775156"

        webClient.get()
            .uri("/account/$fakeIban/balance")
            .exchange()
            .also { response -> response.expectStatus().is5xxServerError }

    }

    @Test
    @DisplayName("should get 404 if an account does not exist (balance check)")
    fun notFoundAccountBalanceTest() {

        val fakeIban = "IT11U0300203280797111426236"
        whenever(accountService.getAccountBalance(any(), anyOrNull())).thenThrow(AccountNotFoundException::class.java)

        webClient.get()
            .uri("/account/$fakeIban/balance")
            .exchange()
            .also { response -> response.expectStatus().isNotFound }

        verify(accountService).getAccountBalance(fakeIban, null)
    }

    @Test
    @DisplayName("should transfer some money from an account to another")
    fun transferMoneyTest() {
        val fromIban = "IT11U0300203280797111426236"
        val toIban = "IT12H0300203280482631733322"
        whenever(accountService.transfer(any())).thenReturn(transferredResponse())

        webClient.patch()
            .uri("/account/transfer")
            .body(wireTransfer(from = fromIban, to = toIban))
            .exchange()
            .also { response -> response.expectBody<WireTransferResponse>().isEqualTo(transferredResponse()) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).transfer(wireTransfer(from = fromIban, to = toIban))
    }

    @Test
    @DisplayName("should not transfer if amount is negative")
    fun amountIsNegativeInTransferTest() {
        val fromIban = "IT11U0300203280797111426236"
        val toIban = "IT12H0300203280482631733322"

        webClient.patch()
            .uri("/account/transfer")
            .body(wireTransfer(from = fromIban, to = toIban, amount = -1.0))
            .exchange()
            .also { response -> response.expectStatus().isBadRequest }

    }

    @Test
    @DisplayName("should not transfer if the first account does not have enough money")
    fun accountDoesNotHaveEnoughMoneyTest() {
        val fromIban = "IT11U0300203280797111426236"
        val toIban = "IT12H0300203280482631733322"
        whenever(accountService.transfer(any())).thenThrow(NotEnoughMoneyException::class.java)

        webClient.patch()
            .uri("/account/transfer")
            .body(wireTransfer(from = fromIban, to = toIban))
            .exchange()
            .also { response -> response.expectStatus().isForbidden }

        verify(accountService).transfer(wireTransfer(from = fromIban, to = toIban))
    }

    @Test
    @DisplayName("should throw 404 if one of the accounts does not exist")
    fun notFoundAccountInTransferTest() {
        val fromIban = "IT11U0300203280797111426236"
        val toIban = "IT12H0300203280482631733322"
        whenever(accountService.transfer(any())).thenThrow(AccountNotFoundException::class.java)

        webClient.patch()
            .uri("/account/transfer")
            .body(wireTransfer(from = fromIban, to = toIban))
            .exchange()
            .also { response -> response.expectStatus().isNotFound }

        verify(accountService).transfer(wireTransfer(from = fromIban, to = toIban))
    }

    @Test
    @DisplayName("should add balance to account")
    fun addBalanceTest(){
        val iban = "IT11U0300203280797111426236"
        whenever(accountService.addBalance(any())).thenReturn(depositResponse())

        webClient.patch()
            .uri("/account/deposit")
            .body(deposit(iban = iban))
            .exchange()
            .also { response -> response.expectBody<DepositResponse>().isEqualTo(depositResponse()) }
            .also { response -> response.expectStatus().isOk }

        verify(accountService).addBalance(deposit(iban = iban))
    }

    @Test
    @DisplayName("should not do deposit if balance is negative")
    fun dontAddBalanceIfNegativeTest(){
        val iban = "IT11U0300203280797111426236"

        webClient.patch()
            .uri("/account/deposit")
            .body(deposit(iban = iban, amount = -1.0))
            .exchange()
            .also { response -> response.expectStatus().isBadRequest }

    }

    @Test
    @DisplayName("should throw 404 if account does not exist when depositing")
    fun notFoundAccountDuringDepositingTest() {
        val iban = "IT11U0300203280797111426236"
        whenever(accountService.addBalance(any())).thenThrow(AccountNotFoundException::class.java)

        webClient.patch()
            .uri("/account/deposit")
            .body(deposit(iban = iban))
            .exchange()
            .also { response -> response.expectStatus().isNotFound }

        verify(accountService).addBalance(deposit(iban = iban))
    }
}