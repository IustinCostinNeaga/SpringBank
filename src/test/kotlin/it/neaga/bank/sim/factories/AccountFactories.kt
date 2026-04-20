package it.neaga.bank.sim.factories

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
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
        ) = Account(
            iban = iban,
            name = "Dario",
            surname = "Lampa",
            email = "lampa.dario@example.it",
            phone = "+39123123123",
            password = "aPassword",
            defaultCurrency = defaultCurrency,
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

}