package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;

import java.util.*;

public class PageImpl implements Page {

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
        return lines.size();
    }

    @Override
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return lines.contains(o);
    }

    @Override
    public Iterator<Line> iterator() {
        return lines.iterator();
    }

    @Override
    public Object[] toArray() {
        return lines.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return lines.toArray(a);
    }

    @Override
    public boolean add(Line line) {
        return lines.add(line);
    }

    @Override
    public boolean remove(Object o) {
        return lines.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return lines.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Line> c) {
        return lines.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Line> c) {
        return lines.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return lines.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return lines.retainAll(c);
    }

    @Override
    public void clear() {
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
    public void add(int index, Line line) {
        lines.add(index, line);
    }

    @Override
    public Line remove(int index) {
        return lines.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return lines.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lines.lastIndexOf(o);
    }

    @Override
    public ListIterator<Line> listIterator() {
        return lines.listIterator();
    }

    @Override
    public ListIterator<Line> listIterator(int index) {
        return lines.listIterator(index);
    }

    @Override
    public List<Line> subList(int fromIndex, int toIndex) {
        return lines.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageImpl lines1 = (PageImpl) o;
        return Objects.equals(lines, lines1.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
