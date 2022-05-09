package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;

/**
 * Describes a page in a script. It contains all lines
 * that start on this page.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Page extends List<Line> {

    /**
     * Gets the index of this page.
     *
     * @return the index
     */
    Long getIndex();

    /**
     * Sets the index of this page.
     *
     * @param value the index
     */
    void setIndex(Long value);

    /**
     * Gets all the lines of this page.
     *
     * @return all the lines of this page
     */
    List<Line> getLines();
}
