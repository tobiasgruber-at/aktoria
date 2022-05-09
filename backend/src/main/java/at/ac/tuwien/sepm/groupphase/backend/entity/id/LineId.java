package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Page;

import java.io.Serializable;

/**
 * Id class for lines.
 *
 * @author Marvin Flandorfer
 */
public class LineId implements Serializable {

    private Page page;
    private Long id;

    public LineId() {
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
