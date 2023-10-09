package be.ucll.ipminor341t.springrest.boat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BoatEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenANonValidEmailIsUsed() {
        BoatBodyValue value = new BoatBodyValue();
        value.name = "TestName";
        value.email = "testemail";
        value.insurance = "Insurance1";
        value.length = 1.0;
        value.width = 1.0;
        value.height = 1.0;

        client.post()
                .uri("/api/boat/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"email\":\"email.not.valid\"}");
    }

    private static class BoatBodyValue {
        public String name;
        public String email;
        public String insurance;
        public double length;
        public double width;
        public double height;
    }
}
