package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.id.PageId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity class for pages.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "page")
@IdClass(PageId.class)
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
    @PrimaryKeyJoinColumn
    private Script script;

    @Column(name = "index", nullable = false)
    private Long index;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "page")
    @OrderBy("index desc")
    private List<Line> lines;
}
