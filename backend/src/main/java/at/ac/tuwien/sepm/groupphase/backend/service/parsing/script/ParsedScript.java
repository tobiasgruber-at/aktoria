package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl.PageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A parsed script.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ParsedScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final List<Line> lines;
    private final List<String> roles;
    private List<Page> pages;

    public ParsedScript(List<Line> lines, List<String> roles) {
        this.lines = lines;
        this.roles = roles;
        indexPages();
    }

    /**
     * Gets the lines of this parsed script.
     * <br>
     * Returns a list of all lines in this parsed script.
     *
     * @return all lines
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * Gets the roles of this parsed script.
     * <br>
     * Returns a list of all role names of this parsed script.
     *
     * @return all roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Gets the pages of this parsed script.
     * <br>
     * Returns a list of all pages of this parsed script.
     *
     * @return all pages
     */
    public List<Page> getPages() {
        return pages;
    }

    private void indexPages() {
        LOGGER.trace("indexPages()");

        pages = new LinkedList<>();

        int previousPageIndex = 0;
        int curPageIndex = 0;

        Page curPage = new PageImpl();

        for (Line l : lines) {
            curPageIndex = l.getPage();
            if (previousPageIndex != curPageIndex) {
                pages.add(curPage);
                curPage = new PageImpl();
                previousPageIndex = curPageIndex;
            }
            curPage.add(l);
        }

        pages.add(curPage);
    }

    @Override
    public String toString() {
        LOGGER.trace("toString()");

        StringJoiner stringJoiner = new StringJoiner("\n");

        for (Line p : lines) {
            stringJoiner.add(p.toString());
        }

        return stringJoiner.toString();
    }

    @Override
    public boolean equals(Object o) {
        LOGGER.trace("equals(o = {})", o);

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParsedScript that = (ParsedScript) o;
        return Objects.equals(lines, that.lines) && Objects.equals(roles, that.roles) && Objects.equals(pages, that.pages);
    }

    @Override
    public int hashCode() {
        LOGGER.trace("hashCode()");

        return Objects.hash(lines, roles, pages);
    }
}
