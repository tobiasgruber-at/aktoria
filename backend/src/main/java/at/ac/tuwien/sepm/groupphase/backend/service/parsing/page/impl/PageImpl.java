package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * An implementation of Page.
 *
 * @author Simon Josef Kreuzpointner
 */
@Slf4j
public class PageImpl implements Page {
    private final List<Line> lines;
    private Long index;

    public PageImpl(List<Line> lines, Long index) {
        this.lines = lines;
        this.index = index;
    }

    public PageImpl() {
        this.lines = new LinkedList<>();
    }

    @Override
    public Long getIndex() {
        return index;
    }

    @Override
    public void setIndex(Long value) {
        index = value;
    }

    @Override
    public List<Line> getLines() {
        return lines;
    }

    @Override
    public int size() {
        log.trace("size()");

        return lines.size();
    }

    @Override
    public boolean isEmpty() {
        log.trace("isEmpty()");

        return lines.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        log.trace("contains(o = {})", o);

        return lines.contains(o);
    }

    @Override
    public Iterator<Line> iterator() {
        log.trace("iterator()");

        return lines.iterator();
    }

    @Override
    public Object[] toArray() {
        log.trace("toArray()");

        return lines.toArray();
    }

    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        log.trace("toArray(a = {})", a);

        return lines.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        log.trace("remove(o = {})", o);

        return lines.remove(o);
    }

    @Override
    public Line remove(int index) {
        log.trace("remove(index = {})", index);

        return lines.remove(index);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        log.trace("containsAll(c = {})", c);

        return lines.containsAll(c);
    }

    @Override
    public boolean add(Line line) {
        log.trace("add(line = {})", line);

        return lines.add(line);
    }

    @Override
    public void add(int index, Line line) {
        log.trace("add(index = {}, line = {})", index, line);

        lines.add(index, line);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Line> c) {
        log.trace("addAll(c = {})", c);

        return lines.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Line> c) {
        log.trace("addAll(index = {}, c = {})", index, c);

        return lines.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        log.trace("removeAll(c = {})", c);

        return lines.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        log.trace("retainAll(c = {})", c);

        return lines.retainAll(c);
    }

    @Override
    public void clear() {
        log.trace("clear()");

        lines.clear();
    }

    @Override
    public Line get(int index) {
        return lines.get(index);
    }

    @Override
    public Line set(int index, Line line) {
        return lines.set(index, line);
    }

    @Override
    public int indexOf(Object o) {
        log.trace("indexOf(o = {})", o);

        return lines.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        log.trace("lastIndexOf(o = {})", o);

        return lines.lastIndexOf(o);
    }

    @Override
    public ListIterator<Line> listIterator() {
        log.trace("listIterator()");

        return lines.listIterator();
    }

    @Override
    public ListIterator<Line> listIterator(int index) {
        log.trace("listIterator(index = {})", index);

        return lines.listIterator(index);
    }

    @Override
    public List<Line> subList(int fromIndex, int toIndex) {
        log.trace("subList(fromIndex = {}, toIndex = {})", fromIndex, toIndex);

        return lines.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageImpl lines1 = (PageImpl) o;
        return Objects.equals(lines, lines1.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
