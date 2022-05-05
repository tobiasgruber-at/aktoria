package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;

/**
 * @author Simon Josef Kreuzpointner
 *
 * Discribes a page in a script. It contains all lines
 * that start on this page.
 */
public interface Page extends List<Line> {

    /**
     * Gets all the lines of this page.
     *
     * @return all the lines of this page
     */
    List<Line> getLines();
}
