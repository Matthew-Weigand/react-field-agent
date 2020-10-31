package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository securityClearanceRepository;
    // To determine if securityClearance is being used anywhere before deletion
    private final AgencyAgentRepository agencyAgentRepository;

    public SecurityClearanceService(SecurityClearanceRepository securityClearanceRepository, AgencyAgentRepository agencyAgentRepository) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    public List<SecurityClearance> findAll() {
        return securityClearanceRepository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {
        return securityClearanceRepository.findById(securityClearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {

        Result<SecurityClearance> result = validate(securityClearance);

        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("Cannot set own security clearance", ResultType.INVALID);
            return result;
        }

        securityClearance = securityClearanceRepository.createClearance(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {

        Result<SecurityClearance> result = validate(securityClearance);

        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("Valid ID needed to update security clearance", ResultType.INVALID);
            return result;
        }

        if (!securityClearanceRepository.updateClearance(securityClearance)) {
            String msg = String.format("Unable to located security clearance ID: %s", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<SecurityClearance> deleteById(int securityClearanceId) {

        Result<SecurityClearance> result = new Result<>();
        SecurityClearance deletedClearance = findById(securityClearanceId);
        if (deletedClearance == null) {
            result.addMessage("Security Clearance ID not located", ResultType.NOT_FOUND);
            return result;
        }

        List<AgencyAgent> allAgencyAgents = agencyAgentRepository.findAll();

        for (AgencyAgent iterator : allAgencyAgents) {
            if (iterator.getSecurityClearance().getSecurityClearanceId() == securityClearanceId) {
                result.addMessage("Cannot delete a security clearance currently in use", ResultType.INVALID);
                return result;
            }
        }

        if (securityClearanceRepository.deleteClearanceById(securityClearanceId)) {
            result.setPayload(deletedClearance);
            result.addMessage("Security Clearance successfully" + securityClearanceId + ",deleted", ResultType.SUCCESS);
            return result;
        }

        return null;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {

        Result<SecurityClearance> result = new Result<>();

        if (securityClearance == null) {
            result.addMessage("Security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("Security clearance name is required", ResultType.INVALID);
        }

        for (SecurityClearance iteratedClearance : findAll()) {
            if (securityClearance.getName() == iteratedClearance.getName()
                    && securityClearance != iteratedClearance) {
                result.addMessage("Security clearance name must be unique", ResultType.INVALID);
                return result;
            }
        }
        return result;
    }

}