package learn.field_agent.data;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAll() {
        List<Alias> all = repository.findAll();
        assertNotNull(all);
        assertEquals(3, all.size());
    }

    @Test
    void shouldFindOneForAgentOne() {
        List<Alias> agentOnesAliases = repository.findAllAgents(1);
        assertNotNull(agentOnesAliases);
        assertEquals(1, agentOnesAliases.size());
    }

    @Test
    void shouldFindById() {
        Alias expected = new Alias(2, "Sherlock Holmes", "Sherly", 2);
        Alias actual = repository.findById(2);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindMissing() {
        assertNull(repository.findById(2000));
    }

    @Test
    void shouldAdd() {

        Alias alias = new Alias();
        alias.setName("Carmen Sandiego");
        alias.setPersona("Red Dress Redemption");
        alias.setAgentId(4);

        Alias actual = repository.createAlias(alias);
        assertNotNull(actual);
        assertEquals(alias, actual);
        assertEquals(4, actual.getAliasId());
    }

    @Test
    void shouldUpdateExisting() {
        Alias alias = new Alias(1, "Carmen Sandiego", "Red Dead Redemption", 3);
        assertTrue(repository.updateAlias(alias));
        assertEquals("Red Dead Redemption", repository.findById(1).getPersona());
    }

    @Test
    void shouldNotUpdateMissingAlias() {
        assertFalse(repository.updateAlias(new Alias(35, "Carmen Las Vegas", "What Happened", 3)));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteAliasById(3));
    }

    @Test
    void shouldNotDelete() {
        assertFalse(repository.deleteAliasById(55));
    }
}