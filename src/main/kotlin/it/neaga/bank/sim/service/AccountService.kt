package it.neaga.bank.sim.service

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.BalanceResponse
import it.neaga.bank.sim.dto.response.NewAccountResponse
import it.neaga.bank.sim.model.Account
import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.repository.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

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
class AccountService(private val ibanGenerator: IbanGenerator, private val accountRepository: AccountRepository) {

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
        TODO()
    }

}