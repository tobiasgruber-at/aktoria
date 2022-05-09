package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Id class for pages.
 *
 * @author Marvin Flandorfer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageId implements Serializable {
    private Script script;
    private Long id;
}
