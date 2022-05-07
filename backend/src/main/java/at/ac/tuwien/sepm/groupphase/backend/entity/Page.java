package at.ac.tuwien.sepm.groupphase.backend.entity;

public class Page {

    private Long id;
    private Script script;
    private Long index;

    public Page(Long id, Script script, Long index) {
        this.id = id;
        this.script = script;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
}
