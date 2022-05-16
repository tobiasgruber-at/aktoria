package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "start", updatable = false, columnDefinition = "datetime default current_timestamp")
    private Timestamp start;

    @Column(name = "end", updatable = false)
    private LocalDateTime end;

    @Column(name = "self_assessement")
    private AssessmentType selfAssessment;

    @Column(name = "deprecated", columnDefinition = "boolean default false")
    private Boolean deprecated;

    @Column(name = "coverage")
    private Double coverage;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "section", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "current_line", nullable = false)
    private Line currentLine;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role", nullable = false)
    private Role role;
}
