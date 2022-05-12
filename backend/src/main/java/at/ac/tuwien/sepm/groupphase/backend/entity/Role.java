package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.Color;
import java.util.List;

/**
 * Entity class for roles.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "script", nullable = false)
    private Script script;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "color")
    private Color color;

    @ManyToMany(mappedBy = "spokenBy")
    private List<Line> lines;
}
