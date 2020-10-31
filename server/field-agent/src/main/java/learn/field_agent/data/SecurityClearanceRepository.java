package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import java.util.List;

public interface SecurityClearanceRepository {
    SecurityClearance findById(int securityClearanceId);

    List<SecurityClearance> findAll();

    SecurityClearance createClearance(SecurityClearance securityClearance);

    boolean updateClearance(SecurityClearance securityClearance);

    boolean deleteClearanceById(int security_id);

}