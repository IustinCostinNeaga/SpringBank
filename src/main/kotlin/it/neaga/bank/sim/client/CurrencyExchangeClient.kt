package it.neaga.bank.sim.client

import it.neaga.bank.sim.model.Currency
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDate

@Service
class CurrencyExchangeClient(restClientBuilder: RestClient.Builder, @Value("\${external-api.exchange.base-url}") baseUrl: String,) {

    private val restClient: RestClient = restClientBuilder.baseUrl(baseUrl).build()

    fun getRate(from: Currency, to: Currency): Double {
        return restClient.get()
            .uri("/rate/$from/$to")
            .retrieve()
            .body<ExchangeRateResponse>()?.rate ?: throw ChangeSetPersister.NotFoundException()
    }
}


data class ExchangeRateResponse(
    val date: LocalDate,
    val base: String,
    val quote: String,
    val rate: Double
)