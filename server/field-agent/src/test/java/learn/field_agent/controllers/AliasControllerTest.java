package learn.field_agent.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AliasControllerTest {

    @MockBean
    AliasRepository repository;

    @Autowired
    MockMvc mvc;

    List<Alias> all = List.of(
            new Alias(1, "Mr Spy", "The spy", 1),
            new Alias(2, "Sherlock Holmes", "Sherly", 2),
            new Alias(3, "Pink Panther", "Soft Paw", 3)
    );

    List<Alias> allAliases = List.of(new Alias(1, "Mr Spy", "The spy", 1));

    @Test
    void shouldGetAliases() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(all);

        when(repository.findAll()).thenReturn(all);

        mvc.perform(get("/api/alias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldGetAliasAgent() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(allAliases);

        when(repository.findAllAgents(1)).thenReturn(allAliases);

        mvc.perform(get("/api/alias/agent/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldGetByAliasId() throws Exception {
        Alias expected = new Alias(1, "Mr Spy", "The spy", 1);

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(expected);

        when(repository.findById(1)).thenReturn(expected);

        mvc.perform(get("/api/alias/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotGetNullAliasId() throws Exception {

        var request = get("/api/alias/30")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotGetNullAgentId() throws Exception {

        when(repository.findAllAgents(30)).thenReturn(null);

        var request = get("/api/alias/agent/30")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddAlias() throws Exception {
        Alias aliasIn = new Alias(0, "Random Spy", "Nothing Random About Him", 1);
        Alias expected = new Alias(1, "Random Spy", "Nothing Random About Him", 1);

        when(repository.createAlias(any())).thenReturn(expected);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(aliasIn);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/alias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotAddEmptyAliasName() throws Exception {

        Alias aliasIn = new Alias(0, "", "", 1);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(aliasIn);

        var request = post("/api/alias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturnEmpty400() throws Exception {

        var request = post("/api/alias")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}