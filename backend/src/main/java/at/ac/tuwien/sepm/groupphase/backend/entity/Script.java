package at.ac.tuwien.sepm.groupphase.backend.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 * Entity class for scripts.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "script")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "script", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("index desc")
    private List<Page> pages;

    @OneToMany(mappedBy = "script", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Role> roles;

    @ManyToMany(mappedBy = "participatesIn")
    private Set<User> participants;
}
