package com.virtuslab.vssjavastats.integration;

import com.virtuslab.vssjavastats.controller.HashTypesView;
import com.virtuslab.vssjavastats.domain.Password;
import com.virtuslab.vssjavastats.domain.StatsRepository;
import com.virtuslab.vssjavastats.events.KafkaEventConsumer;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {StatsServiceIntegrationTest.Initializer.class})
public class StatsServiceIntegrationTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

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

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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

    @Test
    void shouldReturnStatsFromDedicatedEndpoint() throws Exception {
        //given
        statsRepository.save(new Password("MD5", "abc", "!@#"));
        statsRepository.save(new Password("MD5", "def", "!@#"));
        statsRepository.save(new Password("SHA-1", "ghi", "!@#"));
        statsRepository.save(new Password("SHA-1", "jkl", "!@#"));
        statsRepository.save(new Password("SHA-1", "mno", "!@#"));
        statsRepository.save(new Password("test", "prs", "!@#"));

        // when
        this.mockMvc
                .perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].type", is("MD5")))
                .andExpect(jsonPath("$.data[0].amount", is(2)))
                .andExpect(jsonPath("$.data[1].type", is("SHA-1")))
                .andExpect(jsonPath("$.data[1].amount", is(3)))
                .andExpect(jsonPath("$.data[2].type", is("test")))
                .andExpect(jsonPath("$.data[2].amount", is(1)));
    }
}
