package mkaciuba.inggreencode.atmservice.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import mkaciuba.inggreencode.atmservice.dto.ServiceTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        var requests = List.of("example_1_request.json", "example_2_request.json");
        var responses = List.of("example_1_response.json", "example_2_response.json");

        for (int i = 0; i < requests.size(); i++) {
            var request = requests.get(i);
            var response = responses.get(i);

            mvc.perform(post("/atms/calculateOrder")
                            .contentType("application/json")
                            .content(readResourceFile(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(readResourceFile(response)));
        }
    }

    @Test
    void shouldHandleLargeRandomExample() throws Exception {
        mvc.perform(post("/atms/calculateOrder")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(randomTasks())))
                .andExpect(status().isOk());
    }

    private String readResourceFile(String fileName) {
        try {
            return new String(resourceLoader.getResource("classpath:examples/atmservice/" + fileName).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource file: " + fileName, e);
        }
    }

    private List<ServiceTask> randomTasks() {
        var tasks = IntStream.range(0, 999)
                .mapToObj(region -> IntStream.range(1, 99).mapToObj(
                                atmId -> Stream.of(ServiceTask.RequestType.values())
                                        .map(requestType -> new ServiceTask(region, requestType, atmId))
                        ).flatMap(s -> s)
                ).flatMap(s -> s)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(tasks);
        return tasks;
    }
}
