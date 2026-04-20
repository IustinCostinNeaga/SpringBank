package it.neaga.bank.sim.factories

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.NewAccountResponse
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
        ) = NewAccountResponse(
            IBAN = iban,
            currency = Currency.EUR,
        )
}