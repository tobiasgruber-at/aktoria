package at.ac.tuwien.sepm.groupphase.backend.service.parsing.page;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;

public interface Page extends List<Line> {
    List<Line> getLines();
}
