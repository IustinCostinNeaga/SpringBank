package it.neaga.bank.sim.client

import it.neaga.bank.sim.model.Currency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@SpringBootTest
class CurrencyExchangeClientTest(@Autowired val currencyExchangeClient: CurrencyExchangeClient) {

    @Autowired
    lateinit var server: MockRestServiceServer


    @Test
    @DisplayName("should get excange rate from server")
    fun getExchangeRate(){
        server.expect(requestTo("https://api.frankfurter.dev/v2/rate/EUR/USD"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON))

        val rate = currencyExchangeClient.getRate(Currency.EUR, Currency.USD)

        assertThat(rate).isEqualTo(1.1769)
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