package mkaciuba.inggreencode.onlinegame.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import mkaciuba.inggreencode.onlinegame.dto.Clan;
import mkaciuba.inggreencode.onlinegame.dto.Players;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OnlineGameReportResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnCorrectResponseForExampleRequests() throws Exception {
        String request = "example_request.json";
        String response = "example_response.json";

        mvc.perform(post("/onlinegame/calculate")
                        .contentType("application/json")
                        .content(readResourceFile(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(readResourceFile(response)));
    }

    @Test
    void shouldHandleLargeRandomExample() throws Exception {
        mvc.perform(post("/onlinegame/calculate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(randomPlayers())))
                .andExpect(status().isOk());
    }

    private String readResourceFile(String fileName) {
        try {
            return new String(resourceLoader.getResource("classpath:examples/onlinegame/" + fileName).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource file: " + fileName, e);
        }
    }

    private Players randomPlayers() {
        var random = new Random();
        var groupSize = random.nextInt(1, 1000);
        var maxClanMembers = random.nextInt(1, groupSize);

        var clans = IntStream.range(1, 20000).mapToObj(i -> new Clan(random.nextInt(1, maxClanMembers), random.nextInt(1, 100000))).toList();
        return new Players(groupSize, clans);
    }
}
