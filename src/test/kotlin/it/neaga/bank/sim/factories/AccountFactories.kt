package it.neaga.bank.sim.factories

import it.neaga.bank.sim.dto.request.DepositRequest
import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.request.WireTransferRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.DepositResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency

object AccountFactories {
        fun newAccountRequest() = NewAccountRequest(
            name = "Dario",
            surname = "Lampa",
            email = "lampa.dario@example.it",
            phone = "123-123-123",
            password = "aLongPasswordThatIsAtLeast10Characters",
            defaultCurrency = Currency.EUR
        )

        fun newAccountResponse(
            iban : String = "IT52G0300203280939318827487",
            currency : Currency = Currency.EUR,
        ) = NewAccountResponse(
            IBAN = iban,
            currency = currency,
        )

        fun account(
            iban : String = "IT52G0300203280939318827487",
            defaultCurrency: Currency = Currency.EUR,
            balance : Double = 0.0,
        ) = Account(
            iban = iban,
            name = "Dario",
            surname = "Lampa",
            email = "lampa.dario@example.it",
            phone = "123-123-123",
            password = "aLongPasswordThatIsAtLeast10Characters",
            defaultCurrency = defaultCurrency,
            balance = balance,
        )

    fun balance(
        balance : Double = 10.0,
        currency : Currency = Currency.EUR,
        iban : String = "IT52G0300203280939318827487",
    ) = BalanceResponse(
        balance = balance,
        iban = iban,
        currency = currency
    )

    fun transferredResponse(
        amountTransferred : Double = 10.0,
        currencySent: Currency = Currency.EUR,
        rate: Double = 1.5,
        currencyArrived: Currency = Currency.USD,
        accountAfterTransfer: Account = account(balance =10.0),
    ) = WireTransferResponse(
        amountSent = amountTransferred,
        currencySent = currencySent,
        amountArrived = amountTransferred * rate,
        currencyArrived = currencyArrived,
        accountAfterTransfer = accountAfterTransfer
    )

    fun wireTransfer(
        from: String = "anIban",
        to: String = "anotherIban",
        amount: Double = 1.0,
    ) = WireTransferRequest(
        from = from,
        to = to,
        amount = amount,
    )

    fun deposit(
        iban: String = "IT52G0300203280939318827487",
        amount: Double = 1.0,
        currency: Currency = Currency.EUR,
    ) = DepositRequest(
        iban = iban,
        amount = amount,
        currency = currency
    )

    fun depositResponse(
        amount: Double = 1.0,
        currency: Currency = Currency.EUR,
        convertedAmount: Double? = null,
        accountCurrency: Currency? = null,
        rate: Double? = null,
        account: Account = account(balance = 11.5),
    ) = DepositResponse(
        amount = amount,
        currency = currency,
        convertedAmount = convertedAmount,
        accountCurrency = accountCurrency,
        rate = rate,
        accountAfterDeposit = account
    )

}