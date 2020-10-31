package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;

    List<Alias> all = List.of(
            new Alias(1, "Mr Spy", "The spy", 1),
            new Alias(2, "Sherlock Holmes", "Sherly", 2),
            new Alias(3, "Pink Panther", "Soft Paw", 3)
    );

    List<Alias> allAliases = List.of(new Alias(1, "Mr Spy", "The spy", 1));

    @Test
    void shouldGetAll() {
        when(repository.findAll()).thenReturn(all);
        assertEquals(3, service.findAll().size());
    }

    @Test
    void shouldFindOneForAgentOne() {
        when(repository.findAllAgents(1)).thenReturn(allAliases);
        assertEquals(1, service.findAllAgents(1).size());
    }

    @Test
    void shouldFindById() {
        Alias alias = new Alias(1, "Mr Spy", "The spy", 1);
        when(repository.findById(1)).thenReturn(alias);
        Alias actual = service.findById(1);
        assertEquals(alias, actual);
    }

    @Test
    void shouldNotFindNotExisting() {
        when(repository.findById(40)).thenReturn(null);
        assertNull(service.findById(40));
    }

    @Test
    void shouldAdd() {
        Alias aliasIn = new Alias(0, "Random Spy", "Nothing Random About Him", 1);
        Alias mockOut = new Alias(1, "Random Spy", "Nothing Random About Him", 1);
        when(repository.createAlias(aliasIn)).thenReturn(mockOut);

        Result<Alias> actual = service.add(aliasIn);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddBlankAliasName() {
        Alias blankAlias = new Alias(0, "", "", 1);
        Result<Alias> actual = service.add(blankAlias);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotAddDuplicateNullPersona() {
        when(repository.findAll()).thenReturn(all);

        Alias duplicateAlias = new Alias(1, "Mr Spy", "", 1);

        Result<Alias> actual = service.add(duplicateAlias);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdateAlias() {
        Alias alias = new Alias(3, "Pink Panther", "Panther Paw", 1);
        when(repository.updateAlias(alias)).thenReturn(true);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateEmptyName() {
        Alias empty = new Alias(1, "", "", 1);
        Result<Alias> actual = service.update(empty);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Alias badAlias = new Alias(55, "The MIA", "Where is He", 1);
        when(repository.findAll()).thenReturn(all);
        Result<Alias> actual = service.update(badAlias);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(repository.deleteAliasById(2)).thenReturn(true);
        assertTrue(repository.deleteAliasById(2));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteAliasById(50));
    }
}