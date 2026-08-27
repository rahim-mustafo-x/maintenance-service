package org.safa.maintenanceservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.safa.maintenanceservice.models.model.UserRole;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        properties = {
        "JWT_SECRET_KEY=AEbC+WPQahwC15p5VQepppEQJeifSxg6/5Uy06Apej0=",
        "REDIS_PORT=6379",
        "spring.data.redis.host=localhost",
        "TELEGRAM_BOT_URL=http://localhost:8081"
}
)
class MaintenanceServiceApplicationTests {

    @ServiceConnection
    private static final PostgreSQLContainer container = new PostgreSQLContainer("postgres:18");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setAPI(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        //we will start it temporarily
        container.start();
    }

    @Test
    void contextLoads() {
        //prefer to test creation of api for default
        var requestBody = """
                {\s
                    "fullName":"%s",
                    "username":"%s",
                    "phoneNumber":"%s",
                    "password":"%s",
                    "role":"%s"
                }
               \s""".formatted("Mustafo Rahim", "rahim.mustafo.x", "+998905579765", "mustafo18122009", UserRole.HOME_OWNER.name());
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

    }

}
