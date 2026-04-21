package it.neaga.bank.sim.service

import it.neaga.bank.sim.client.CurrencyExchangeClient
import it.neaga.bank.sim.dto.request.DepositRequest
import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.request.WireTransferRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.DepositResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.repository.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class IbanGenerator {
    fun generateItIban(): String {

        val nationCode = "IT"
        val cinEU = (0..99).random().toString().padStart(2, '0')
        val cin = randomUpperLetter().toString()
        val abi = (0..9999999).random().toString().padStart(5, '0')
        val cab = (0..9999999).random().toString().padStart(5, '0')
        val ccNumber = (0..12).joinToString("") { randomAlphaNumeric().toString() }

        return nationCode + cinEU + cin + abi + cab + ccNumber
    }

    private fun randomUpperLetter(): Char = ('A'..'Z').random()

    private fun randomAlphaNumeric(): Char = if ((0..1).random() == 0) randomUpperLetter() else ('0'..'9').random()
}

@Service
class AccountService(
    private val ibanGenerator: IbanGenerator,
    private val accountRepository: AccountRepository,
    private val currencyExchangeClient: CurrencyExchangeClient
) {

    fun createNewAccount(newAccount: NewAccountRequest): NewAccountResponse {

        val iban = ibanGenerator.generateItIban()

        val account = accountRepository.save(
            Account(
                iban = iban,
                name = newAccount.name,
                surname = newAccount.surname,
                email = newAccount.email,
                phone = newAccount.phone,
                password = newAccount.password,
                defaultCurrency = newAccount.defaultCurrency,
            )
        )

        return NewAccountResponse(
            IBAN = account.iban,
            currency = account.defaultCurrency
        )
    }

    fun getAccount(iban: String): Account {
        return accountRepository.getReferenceById(iban)
    }

    fun getAccountBalance(iban: String, currency: Currency?): BalanceResponse {
        val account = accountRepository.getReferenceById(iban)
        val response = if (currency == null) {
            BalanceResponse(
                balance = account.balance,
                iban = account.iban,
                currency = account.defaultCurrency
            )
        } else {
            val rate = currencyExchangeClient.getRate(account.defaultCurrency, currency)
            BalanceResponse(
                balance = rate * account.balance,
                iban = account.iban,
                currency = currency
            )
        }
        return response
    }

    fun transfer(wireTransferRequest: WireTransferRequest): WireTransferResponse {
        val fromAccount = accountRepository.getReferenceById(wireTransferRequest.from)
        val toAccount = accountRepository.getReferenceById(wireTransferRequest.to)

        val amountToRemove = wireTransferRequest.amount
        val conversionRate =
            if (fromAccount.defaultCurrency == toAccount.defaultCurrency) 1.0
            else currencyExchangeClient.getRate(fromAccount.defaultCurrency, toAccount.defaultCurrency)

        val amountToAdd = amountToRemove * conversionRate

        val fromAccountWithBalanceChanged = accountRepository.save(fromAccount.withdraw(amountToRemove))
        val toAccountWithBalanceChanged = accountRepository.save(toAccount.deposit(amountToAdd))

        return WireTransferResponse(
            amountSent = amountToRemove,
            currencySent = fromAccountWithBalanceChanged.defaultCurrency,
            amountArrived = amountToAdd,
            currencyArrived = toAccountWithBalanceChanged.defaultCurrency,
            accountAfterTransfer = fromAccountWithBalanceChanged
        )
    }

    fun addBalance(depositRequest: DepositRequest): DepositResponse {
        val iban = depositRequest.iban
        val account = accountRepository.getReferenceById(iban)

        println(account)

        val conversionRate =
            if(account.defaultCurrency == depositRequest.currency) 1.0
            else currencyExchangeClient.getRate(depositRequest.currency, account.defaultCurrency)

        val amountToAdd = depositRequest.amount * conversionRate
        val updatedAccount = accountRepository.save(account.deposit(amountToAdd))

        return DepositResponse(
            amount = depositRequest.amount,
            currency = depositRequest.currency,
            rate = conversionRate,
            accountAfterDeposit = updatedAccount
        )
    }

}