package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity class for lines.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "line")
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
    @JoinColumn(name = "page", nullable = false)
    private Page page;

    @Column(name = "index", nullable = false)
    private Long index;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "audio")
    private String audio;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "spoken_by", joinColumns = @JoinColumn(name = "line_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> spokenBy;
}
