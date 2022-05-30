package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.listener.SessionListener;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EntityListeners(SessionListener.class)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "start_time", updatable = false, nullable = false,
        columnDefinition = "timestamp without time zone default current_timestamp")
    private LocalDateTime start;

    @Column(name = "end_time", updatable = false)
    private LocalDateTime end;

    @Column(name = "self_assessement")
    private AssessmentType selfAssessment;

    @Column(name = "deprecated", columnDefinition = "boolean default false")
    private Boolean deprecated = false;

    @Column(name = "coverage")
    private Double coverage = 0.0;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "section", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "current_line", nullable = false)
    private Line currentLine;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role")
    private Role role;

    @PreRemove
    public void removeSession() {
        log.info("delete Session: " + id);
    }
}
