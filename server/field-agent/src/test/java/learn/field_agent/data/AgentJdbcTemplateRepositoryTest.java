package learn.field_agent.data;

import learn.field_agent.models.Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AgentJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 9;

    @Autowired
    AgentJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Agent> agents = repository.findAll();
        assertNotNull(agents);
        assertTrue(agents.size() == 7 || agents.size() == 8); // one possible deletion
    }

    @Test
    void shouldFindHazel() {
        Agent hazel = repository.findById(1);
        assertEquals(1, hazel.getAgentId());
        assertEquals("Hazel", hazel.getFirstName());
        assertEquals(2, hazel.getAgencies().size());
    }

    @Test
    void shouldAdd() {
        Agent agent = makeAgent();
        Agent actual = repository.add(agent);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getAgentId());
    }

    @Test
    void shouldUpdate() {
        Agent agent = makeAgent();
        agent.setAgentId(3);
        assertTrue(repository.update(agent));
        agent.setAgentId(13);
        assertFalse(repository.update(agent));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));
    }

    private Agent makeAgent() {
        Agent agent = new Agent();
        agent.setFirstName("Test");
        agent.setLastName("Last Name");
        agent.setDob(LocalDate.of(1985, 8, 15));
        agent.setHeightInInches(66);
        return agent;
    }
}