package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl.PageImpl;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;

/**
 * A parsed script.
 *
 * @author Simon Josef Kreuzpointner
 */
@Slf4j
public class ParsedScript {
    private final List<Line> lines;
    private final List<String> roles;

    private List<Color> colors;
    private List<Page> pages;

    public ParsedScript(List<Line> lines, List<String> roles) {
        this.lines = lines;
        this.roles = roles;
        indexComponents();
        colorRoles();
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

    /**
     * Gets the colors for each role.
     *
     * @return all colors
     */
    public List<Color> getColors() {
        return colors;
    }

    private void indexComponents() {
        log.trace("indexComponents()");

        pages = new LinkedList<>();

        Long previousPageIndex = 0L;
        Long curPageIndex = 0L;
        Long curLineIndex = 0L;

        Page curPage = new PageImpl();
        curPage.setIndex(curPageIndex);

        for (Line l : lines) {
            l.setIndex(curLineIndex++);
            curPageIndex = l.getPage();
            if (!previousPageIndex.equals(curPageIndex)) {
                pages.add(curPage);
                curPage = new PageImpl();
                curPage.setIndex(curPageIndex);
                previousPageIndex++;
            }
            curPage.add(l);
        }

        pages.add(curPage);
    }

    private void colorRoles() {
        Color baseColor = new Color(160, 120, 222);
        colors = new LinkedList<>();

        for (int i = 0; i < roles.size(); i++) {
            colors.add(getPaletteColor(baseColor));
        }
    }

    private Color getPaletteColor(Color base) {
        Random r = new Random();
        return new Color(
            Math.floorDiv((int) (base.getRed() * 0.66f + r.nextInt(200) * 0.33), 2),
            Math.floorDiv((int) (base.getGreen() * 0.66f + r.nextInt(200) * 0.33), 2),
            Math.floorDiv((int) (base.getBlue() * 0.66f + r.nextInt(200) * 0.33), 2)
        );
    }

    @Override
    public String toString() {
        log.trace("toString()");

        StringJoiner stringJoiner = new StringJoiner("\n");

        for (Line p : lines) {
            stringJoiner.add(p.toString());
        }

        return stringJoiner.toString();
    }

    @Override
    public boolean equals(Object o) {
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
        return Objects.hash(lines, roles, pages);
    }
}
