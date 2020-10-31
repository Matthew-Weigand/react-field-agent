package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance ultraSecret = new SecurityClearance(2, "Ultra Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(ultraSecret, actual);

        actual = repository.findById(7);
        assertEquals(null, actual);
    }

    @Test
    void shouldFindAll() {

        List<SecurityClearance> all = repository.findAll();

        assertEquals(2, all.size());
    }


    @Test
    void shouldNotUpdate() {
        SecurityClearance secret = new SecurityClearance(2, "Top Secret");

        repository.updateClearance(secret);
        SecurityClearance expected = new SecurityClearance();
        expected.setSecurityClearanceId(2);
        expected.setName("Top Secret");

        SecurityClearance actual = repository.findById(2);
        assertFalse(expected.getName() == actual.getName());

    }


}