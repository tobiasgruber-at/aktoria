package at.ac.tuwien.sepm.groupphase.backend.entity.id;

import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Id class for lines.
 *
 * @author Marvin Flandorfer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineId implements Serializable {
    private Page page;
    private Long id;
}
