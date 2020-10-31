package learn.field_agent.domain;

import org.springframework.stereotype.Service;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;


import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public List<Alias> findAll() {
        return repository.findAll();
    }

    public List<Alias> findAllAgents(int agentId) {
        return repository.findAllAgents(agentId);
    }

    public Alias findById(int aliasId) {
        return repository.findById(aliasId);
    }

    public Result<Alias> add(Alias alias) {

        Result<Alias> result = validate(alias);

        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() != 0) {
            result.addMessage("Alias id not allowed during add procedure", ResultType.INVALID);
            return result;
        }

        alias = repository.createAlias(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {

        Result<Alias> result = validate(alias);

        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("Alias id required for successful update", ResultType.INVALID);
            return result;
        }

        if (!repository.updateAlias(alias)) {
            String msg = String.format("Alias id: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteAliasById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {

        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("Alias cannot be null", ResultType.INVALID);
            return result;
        }

        for (Alias all : findAll()) {
            if (alias.getName() == all.getName()
                    && alias != all) {
                if (Validations.isNullOrBlank(alias.getPersona())) {
                    result.addMessage("Duplicate names require unique alias'", ResultType.INVALID);
                }
                return result;
            }
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("Alias cannot be empty", ResultType.INVALID);
        }

        return result;
    }

}