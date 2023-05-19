package com.virtuslab.vssjavastats.integration;

import com.virtuslab.vssjavastats.domain.Password;
import com.virtuslab.vssjavastats.domain.StatsRepository;
import com.virtuslab.vssjavastats.events.KafkaEventConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {StoreEventIntegrationTest.Initializer.class})
public class StoreEventIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private KafkaEventConsumer eventConsumer;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void shouldStoreEventFromKafkaInDatabase() {
        // when
        eventConsumer.storeEvent("{\"hashType\":\"MD5\",\"password\":\"123!@#abc\",\"hash\":\"452933f447fe6aaa46cae4860239ca72\"}");

        // then
        final Password result = statsRepository.getByHash("452933f447fe6aaa46cae4860239ca72");
        assertEquals("452933f447fe6aaa46cae4860239ca72", result.hash());
        assertEquals("MD5", result.hashType());
        assertEquals("123!@#abc", result.password());
    }
}
