package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Entity class for pages.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "page")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "script", nullable = false)
    private Script script;

    @Column(name = "index", nullable = false)
    private Long index;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "page")
    @OrderBy("index desc")
    private List<Line> lines;
}
