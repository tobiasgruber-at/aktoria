package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;

import java.io.Serializable;

/**
 * Id class for pages.
 *
 * @author Marvin Flandorfer
 */
public class PageId implements Serializable {
    private Script script;
    private Long id;

    public PageId() {}

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
