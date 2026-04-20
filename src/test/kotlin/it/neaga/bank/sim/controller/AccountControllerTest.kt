package it.neaga.bank.sim.controller

import it.neaga.bank.sim.model.Currency
import it.neaga.bank.sim.dto.request.NewAccountRequest
import it.neaga.bank.sim.dto.response.NewAccountResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class AccountControllerTest(@Autowired var accountController: AccountController, @Autowired private val webClient: RestTestClient) {

    @Test
    @DisplayName("should load AccountController")
    fun loadAccountController() {
        assertThat(accountController).isNotNull
    }

    @Test
    @DisplayName("should create a new account correctlly")
    fun accountCreationTest(){
        webClient.post()
            .uri("/account/new")
            .body(newAccountRequest())
            .exchange()
            .also { response -> response.expectBody<NewAccountResponse>().isEqualTo(newAccountResponse()) }
            .also { response -> response.expectStatus().isOk }
    }



    fun newAccountRequest() = NewAccountRequest(
        name = "Dario",
        surname = "Lampa",
        email = "lampa.dario@example.it",
        phone = "+39123123123",
        password = "aPassword",
        defaultCurrency = Currency.EUR
    )

    fun newAccountResponse() = NewAccountResponse(
        IBAN = "AWRONGFULLYWRITTENIBAN",
        currency = Currency.EUR,
    )
}