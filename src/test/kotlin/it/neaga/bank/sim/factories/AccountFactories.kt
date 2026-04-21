package it.neaga.bank.sim.factories

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.request.WireTransferRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.dto.response.WireTransferResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency

object AccountFactories {
        fun newAccountRequest() = NewAccountRequest(
            name = "Dario",
            surname = "Lampa",
            email = "lampa.dario@example.it",
            phone = "+39123123123",
            password = "aPassword",
            defaultCurrency = Currency.EUR
        )

        fun newAccountResponse(
            iban : String = "IT95V0300203280975296921156",
            currency : Currency = Currency.EUR,
        ) = NewAccountResponse(
            IBAN = iban,
            currency = currency,
        )

        fun account(
            iban : String = "IT95V0300203280975296921156",
            defaultCurrency: Currency = Currency.EUR,
            balance : Double = 0.0,
        ) = Account(
            iban = iban,
            name = "Dario",
            surname = "Lampa",
            email = "lampa.dario@example.it",
            phone = "+39123123123",
            password = "aPassword",
            defaultCurrency = defaultCurrency,
            balance = balance,
        )

    fun balance(
        balance : Double = 10.0,
        currency : Currency = Currency.EUR,
        iban : String = "IT95V0300203280975296921156",
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

}