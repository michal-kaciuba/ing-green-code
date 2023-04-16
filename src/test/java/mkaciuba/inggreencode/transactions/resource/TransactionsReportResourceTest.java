package mkaciuba.inggreencode.transactions.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import mkaciuba.inggreencode.transactions.dto.Transaction;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionsReportResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnCorrectResponseForExampleRequests() throws Exception {
        String request = "example_request.json";
        String response = "example_response.json";

        mvc.perform(post("/transactions/report")
                        .contentType("application/json")
                        .content(readResourceFile(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(readResourceFile(response)));
    }

    @Test
    void shouldHandleLargeRandomExample() throws Exception {
        mvc.perform(post("/transactions/report")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(randomTransactions())))
                .andExpect(status().isOk());
    }

    private String readResourceFile(String fileName) {
        try {
            return new String(resourceLoader.getResource("classpath:examples/transactions/" + fileName).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource file: " + fileName, e);
        }
    }

    private List<Transaction> randomTransactions() {
        Random random = new Random();
        return IntStream.range(0, 100000)
                .mapToObj(i -> new Transaction(
                        RandomStringUtils.randomNumeric(26),
                        RandomStringUtils.randomNumeric(26),
                        BigDecimal.valueOf(random.nextDouble(0, 50000)).setScale(2, RoundingMode.HALF_UP)
                ))
                .collect(Collectors.toList());
    }
}
