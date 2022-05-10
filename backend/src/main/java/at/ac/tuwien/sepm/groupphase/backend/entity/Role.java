package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.id.RoleId;
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
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.awt.Color;

/**
 * Entity class for roles.
 *
 * @author Marvin Flandorfer
 */
@Entity
@Table(name = "role")
@IdClass(RoleId.class)
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
    @PrimaryKeyJoinColumn
    private Script script;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "color")
    private Color color;
}
