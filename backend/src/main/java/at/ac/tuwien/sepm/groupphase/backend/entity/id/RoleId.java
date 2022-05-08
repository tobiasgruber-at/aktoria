package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;

import java.io.Serializable;

public class RoleId implements Serializable {

    private Script script;
    private Long id;

    private RoleId() {}

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
