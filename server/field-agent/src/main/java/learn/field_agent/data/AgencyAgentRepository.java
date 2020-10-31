package learn.field_agent.data;

import learn.field_agent.models.AgencyAgent;

import java.util.List;

public interface AgencyAgentRepository {

    List<AgencyAgent> findAll();

    boolean add(AgencyAgent agencyAgent);

    boolean update(AgencyAgent agencyAgent);

    boolean deleteByKey(int agencyId, int agentId);
}
