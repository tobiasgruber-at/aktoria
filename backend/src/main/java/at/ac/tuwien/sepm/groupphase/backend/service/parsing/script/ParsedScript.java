package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl.PageImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ParsedScript {

    private final List<Line> lines;
    private final List<String> roles;
    private List<Page> pages;

    public ParsedScript(List<Line> lines, List<String> roles) {
        this.lines = lines;
        this.roles = roles;
        indexPages();
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<Page> getPages() {
        return pages;
    }

    private void indexPages() {
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
