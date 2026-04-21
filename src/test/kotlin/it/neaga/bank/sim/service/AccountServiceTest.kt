package it.neaga.bank.sim.service

import it.neaga.bank.sim.client.CurrencyExchangeClient
import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.factories.AccountFactories
import it.neaga.bank.sim.factories.AccountFactories.account
import it.neaga.bank.sim.factories.AccountFactories.balance
import it.neaga.bank.sim.factories.AccountFactories.deposit
import it.neaga.bank.sim.factories.AccountFactories.depositResponse
import it.neaga.bank.sim.factories.AccountFactories.newAccountRequest
import it.neaga.bank.sim.factories.AccountFactories.newAccountResponse
import it.neaga.bank.sim.factories.AccountFactories.wireTransfer
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
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

    @MockitoBean
    private lateinit var currencyExchangeClient: CurrencyExchangeClient


    @Test
    @DisplayName("should create a new account")
    fun accountCreationTest() {

        val fakeIban = "IT95V0300203280975296921156"
        whenever(ibanGenerator.generateItIban()).thenReturn(fakeIban)
        whenever(accountRepository.save(any<Account>())).thenReturn(account())

        val result = accountService.createNewAccount(newAccountRequest())
        assertThat(result).isEqualTo(newAccountResponse(iban = fakeIban))

        verify(ibanGenerator).generateItIban()
        verify(accountRepository).save(account())

    }

    @Test
    @DisplayName("should get a account")
    fun getAccountTest() {

        val fakeIban = "IT95V0300203280975296921156"
        whenever(accountRepository.getReferenceById(any())).thenReturn(account())

        val result = accountService.getAccount(fakeIban)
        assertThat(result).isEqualTo(account(iban = fakeIban))

        verify(accountRepository).getReferenceById(fakeIban)

    }

    @Test
    @DisplayName("should get an account balance with default currency")
    fun getBalanceTest() {

        val fakeIban = "IT95V0300203280975296921156"
        whenever(accountRepository.getReferenceById(any())).thenReturn(account(balance = 10.0))

        val result = accountService.getAccountBalance(fakeIban, null)
        assertThat(result).isEqualTo(balance(balance = 10.0))

        verify(accountRepository).getReferenceById(fakeIban)
        verify(currencyExchangeClient, never()).getRate(any(), any())

    }

    @Test
    @DisplayName("should get an account balance with set currency")
    fun getBalanceWithCurrencyTest() {

        val fakeIban = "IT95V0300203280975296921156"
        whenever(accountRepository.getReferenceById(any())).thenReturn(account(balance = 10.0))
        whenever(currencyExchangeClient.getRate(any(), any())).thenReturn(1.5)

        val result = accountService.getAccountBalance(fakeIban, Currency.USD)
        assertThat(result).isEqualTo(balance(balance = 15.0, currency = Currency.USD))

        verify(accountRepository).getReferenceById(fakeIban)
        verify(currencyExchangeClient).getRate(Currency.EUR, Currency.USD)

    }

    @Test
    @DisplayName("should create a wire transfer from and to account with same currency")
    fun wireTransferWithSameCurrencyTest() {
        val fromFake = "IT95V0300203280975296921156"
        val toFake = "IT95V0300203280975296921167"

        val fromAccountBeforeTransfer = account(balance = 10.0, defaultCurrency = Currency.EUR, iban = fromFake)
        val toAccountBeforeTransfer = account(balance = 10.0, defaultCurrency = Currency.USD, iban = toFake)

        whenever(accountRepository.getReferenceById(any()))
            .thenReturn(fromAccountBeforeTransfer)
            .thenReturn(toAccountBeforeTransfer)

        whenever(currencyExchangeClient.getRate(any(), any())).thenReturn(1.5)

        whenever(accountRepository.save(any<Account>()))
            .thenReturn(fromAccountBeforeTransfer.copy(balance = 0.0))
            .thenReturn(toAccountBeforeTransfer.copy(balance = 25.0))

        val result = accountService.transfer(wireTransfer(from = fromFake, to = toFake, amount = 10.0))
        assertThat(result).isEqualTo(
            AccountFactories.transferredResponse(
                amountTransferred = 10.0,
                currencySent = Currency.EUR,
                rate = 1.5,
                currencyArrived = Currency.USD,
                accountAfterTransfer = fromAccountBeforeTransfer.copy(balance = 0.0)
            )
        )

        verify(accountRepository).getReferenceById(fromFake)
        verify(accountRepository).getReferenceById(toFake)
        verify(currencyExchangeClient).getRate(Currency.EUR, Currency.USD)
        verify(accountRepository).save(fromAccountBeforeTransfer.copy(balance = 0.0))
        verify(accountRepository).save(toAccountBeforeTransfer.copy(balance = 25.0))

    }

    @Test
    @DisplayName("should deposit some money on a account")
    fun depositTest() {

        val fakeIban = "IT95V0300203280975296921156"
        val accountBeforeDeposit = account(balance = 10.0, iban = fakeIban)
        val accountAfterDeposit = accountBeforeDeposit.copy(balance = 17.5)

        whenever(accountRepository.getReferenceById(any())).thenReturn(accountBeforeDeposit)
        whenever(accountRepository.save(any<Account>())).thenReturn(accountAfterDeposit)
        whenever(currencyExchangeClient.getRate(any(), any())).thenReturn(1.5)

        val result = accountService.addBalance(deposit(iban = fakeIban, amount = 5.0, currency = Currency.USD))
        assertThat(result).isEqualTo(depositResponse(amount = 5.0, currency = Currency.USD, account = accountAfterDeposit))

        verify(accountRepository).getReferenceById(fakeIban)
        verify(accountRepository).save(accountAfterDeposit)
        verify(currencyExchangeClient).getRate(Currency.USD, Currency.EUR)

    }

}