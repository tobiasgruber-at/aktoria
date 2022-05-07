package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.sql.Blob;

public class Line {

    private Long id;
    private Page page;
    private Script script;
    private Long index;
    private String content;
    private Blob audio;
    private boolean active;
    private ApplicationUser recordedBy;
}
