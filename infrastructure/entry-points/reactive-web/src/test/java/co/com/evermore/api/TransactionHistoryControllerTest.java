package co.com.evermore.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {TransactionHistoryController.class})
@WebFluxTest
public class TransactionHistoryControllerTest {

//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    public void testCommandName() {
//        webTestClient.get()
//                .uri("/api/path")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class)
//                .value(userResponse -> {
//                            Assertions.assertThat(userResponse).isEqualTo("Hello World");
//                        }
//                );
//    }

}