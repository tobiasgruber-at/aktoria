package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.id.LineId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Set;

/**
 * Entity class for lines.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "line")
@IdClass(LineId.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @PrimaryKeyJoinColumn
    private Page page;

    @Column(name = "index", nullable = false)
    private Long index;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "audio")
    private String audio;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @ManyToMany
    @JoinTable(name = "spoken_by", joinColumns = {@JoinColumn(name = "script_id"), @JoinColumn(name = "page_id"), @JoinColumn(name = "line_id")},
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> spokenBy;
}
