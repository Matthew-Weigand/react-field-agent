package learn.field_agent.data;

import learn.field_agent.models.Alias;

import java.util.List;

public interface AliasRepository {

    List<Alias> findAll();

    List<Alias> findAllAgents(int agentId);

    Alias findById(int aliasId);

    Alias createAlias(Alias alias);

    boolean updateAlias(Alias alias);

    boolean deleteAliasById(int aliasId);
}