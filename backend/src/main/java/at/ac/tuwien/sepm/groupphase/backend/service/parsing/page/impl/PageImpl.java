package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
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
public class PageImpl implements Page {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final List<Line> lines;

    public PageImpl(List<Line> lines) {
        this.lines = lines;
    }

    public PageImpl() {
        this.lines = new LinkedList<>();
    }

    @Override
    public List<Line> getLines() {
        return lines;
    }

    @Override
    public int size() {
        LOGGER.trace("size()");

        return lines.size();
    }

    @Override
    public boolean isEmpty() {
        LOGGER.trace("isEmpty()");

        return lines.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        LOGGER.trace("contains(o = {})", o);

        return lines.contains(o);
    }

    @Override
    public Iterator<Line> iterator() {
        LOGGER.trace("iterator()");

        return lines.iterator();
    }

    @Override
    public Object[] toArray() {
        LOGGER.trace("toArray()");

        return lines.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        LOGGER.trace("toArray(a = {})", a);

        return lines.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        LOGGER.trace("remove(o = {})", o);

        return lines.remove(o);
    }

    @Override
    public Line remove(int index) {
        LOGGER.trace("remove(index = {})", index);

        return lines.remove(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        LOGGER.trace("containsAll(c = {})", c);

        return lines.containsAll(c);
    }

    @Override
    public boolean add(Line line) {
        LOGGER.trace("add(line = {})", line);

        return lines.add(line);
    }

    @Override
    public void add(int index, Line line) {
        LOGGER.trace("add(index = {}, line = {})", index, line);

        lines.add(index, line);
    }

    @Override
    public boolean addAll(Collection<? extends Line> c) {
        LOGGER.trace("addAll(c = {})", c);

        return lines.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Line> c) {
        LOGGER.trace("addAll(index = {}, c = {})", index, c);

        return lines.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        LOGGER.trace("removeAll(c = {})", c);

        return lines.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        LOGGER.trace("retainAll(c = {})", c);

        return lines.retainAll(c);
    }

    @Override
    public void clear() {
        LOGGER.trace("clear()");

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
        LOGGER.trace("indexOf(o = {})", o);

        return lines.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        LOGGER.trace("lastIndexOf(o = {})", o);

        return lines.lastIndexOf(o);
    }

    @Override
    public ListIterator<Line> listIterator() {
        LOGGER.trace("listIterator()");

        return lines.listIterator();
    }

    @Override
    public ListIterator<Line> listIterator(int index) {
        LOGGER.trace("listIterator(index = {})", index);

        return lines.listIterator(index);
    }

    @Override
    public List<Line> subList(int fromIndex, int toIndex) {
        LOGGER.trace("subList(fromIndex = {}, toIndex = {})", fromIndex, toIndex);

        return lines.subList(fromIndex, toIndex);
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
        PageImpl lines1 = (PageImpl) o;
        return Objects.equals(lines, lines1.lines);
    }

    @Override
    public int hashCode() {
        LOGGER.trace("hashCode()");

        return Objects.hash(lines);
    }
}
