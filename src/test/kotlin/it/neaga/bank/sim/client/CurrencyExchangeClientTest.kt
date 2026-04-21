package it.neaga.bank.sim.client

import it.neaga.bank.sim.model.Currency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest
import org.springframework.boot.test.context.PropertyMapping
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertFailsWith

@RestClientTest(CurrencyExchangeClient::class)
@TestPropertySource(properties = ["external-api.exchange.base-url=http://mock-url"])
class CurrencyExchangeClientTest(@Autowired var currencyExchangeClient: CurrencyExchangeClient) {

    @Autowired
    lateinit var server: MockRestServiceServer

    @Test
    @DisplayName("should get exchange rate from server")
    fun getExchangeRate(){
        server.expect(requestTo("http://mock-url/rate/EUR/USD"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON))

        val rate = currencyExchangeClient.getRate(Currency.EUR, Currency.USD)

        assertThat(rate).isEqualTo(1.1769)
    }
    @Test
    @DisplayName("should throw an exception if currency is not correct")
    fun handleExchangeRateException(){
        server.expect(requestTo("http://mock-url/rate/EUR/USD"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND))

        val exception = assertThrows<HttpClientErrorException> {
            currencyExchangeClient.getRate(Currency.EUR, Currency.USD)
        }

        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    val jsonResponse = """
        {
          "date": "2026-04-20",
          "base": "EUR",
          "quote": "USD",
          "rate": 1.1769
        }
    """.trimIndent()

}