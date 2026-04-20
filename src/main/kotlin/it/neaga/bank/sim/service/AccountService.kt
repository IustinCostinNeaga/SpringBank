package it.neaga.bank.sim.service

import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.NewAccountResponse
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
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
class AccountService(private val ibanGenerator: IbanGenerator) {

    fun createNewAccount(newAccount: NewAccountRequest): NewAccountResponse {
        TODO()
    }

}