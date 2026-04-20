package it.neaga.bank.sim.client

import it.neaga.bank.sim.model.Currency
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDate

@Service
class CurrencyExchangeClient(restClientBuilder: RestClient.Builder) {

    fun getRate(from: Currency, to: Currency): Double {
        TODO()
    }
}
