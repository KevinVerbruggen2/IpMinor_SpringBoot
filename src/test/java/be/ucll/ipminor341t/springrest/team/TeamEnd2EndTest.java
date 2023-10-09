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
public class TeamEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenANonValidEmailIsUsed() {
        TeamBodyValue value = new TeamBodyValue();
        value.name = "TestName";
        value.category = "testcategory";
        value.club = "testclub";
        value.passengers = -1;

        client.post()
                .uri("/api/team/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"passengers\":\"passengers.min\"}");
    }

    private static class TeamBodyValue {
        public String name;
        public String category;
        public String club;
        public int passengers;
    }
}
