package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;


    @Test
    void shouldGetAll() {

        List<SecurityClearance> all = List.of(
                new SecurityClearance(1, "Secret"),
                new SecurityClearance(2, "Top Secret")
        );

        when(repository.findAll()).thenReturn(all);

        assertEquals(2, service.findAll().size());
    }

    @Test
    void shouldFindBySecurityClearanceId() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        when(repository.findById(1)).thenReturn(secret);
        SecurityClearance actual = service.findById(1);
        assertEquals(secret, actual);
    }

    @Test
    void shouldNotFindNullSecurityClearance() {
        when(repository.findById(35)).thenReturn(null);
        assertNull(service.findById(35));
    }

    @Test
    void shouldAddSecurityClearance() {
        SecurityClearance newSecurityClearance = new SecurityClearance(0, "Insanely Secret");
        SecurityClearance mockOut = new SecurityClearance(3, "Insanely Secret");
        when(repository.createClearance(newSecurityClearance)).thenReturn(mockOut);

        Result<SecurityClearance> actual = service.add(newSecurityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddInvalidSecurityClearanceName() {
        SecurityClearance emptySecurityClearance = new SecurityClearance(0, "");
        //when(repository.add(newSecurityClearance)).thenReturn(mockOut);

        Result<SecurityClearance> actual = service.add(emptySecurityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotAddDuplicateSecurityClearanceName() {
        SecurityClearance duplicateSecurityClearance = new SecurityClearance(0, "Secret");

        List<SecurityClearance> all = List.of(
                new SecurityClearance(1, "Secret"),
                new SecurityClearance(2, "Top Secret")
        );

        when(repository.findAll()).thenReturn(all);

        Result<SecurityClearance> actual = service.add(duplicateSecurityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        SecurityClearance updatedSecurityClearance = new SecurityClearance(1, "More Secret than Top");

        when(repository.updateClearance(updatedSecurityClearance)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(updatedSecurityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateToExistingName() {
        SecurityClearance existingSecurityClearance = new SecurityClearance(1, "Secret");

        List<SecurityClearance> all = List.of(
                new SecurityClearance(1, "Secret"),
                new SecurityClearance(2, "Top Secret")
        );

        when(repository.findAll()).thenReturn(all);
        when(repository.updateClearance(existingSecurityClearance)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(existingSecurityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotUpdateEmptySecurityClearanceName() {
        SecurityClearance badSecurityClearance = new SecurityClearance(1, "");
        Result<SecurityClearance> actual = service.update(badSecurityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        SecurityClearance missingSecurityClearance = new SecurityClearance(35, "Most Secret of All");

        List<SecurityClearance> all = List.of(
                new SecurityClearance(1, "Secret"),
                new SecurityClearance(2, "Top Secret")
        );

        when(repository.findAll()).thenReturn(all);
        when(repository.updateClearance(missingSecurityClearance)).thenReturn(false);

        Result<SecurityClearance> actual = service.update(missingSecurityClearance);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotDeleteNullSecurityClearance() {
        when(repository.deleteClearanceById(40)).thenReturn(false);
        Result<SecurityClearance> result = service.deleteById(40);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }
}