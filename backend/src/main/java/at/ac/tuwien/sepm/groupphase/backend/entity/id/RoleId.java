package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Id class for roles.
 *
 * @author Marvin Flandorfer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleId implements Serializable {
    private Script script;
    private Long id;
}
