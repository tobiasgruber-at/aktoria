package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Page Data Access Object.
 * <br>
 * In contrast to the simple page DTO this DTO holds a reference
 * to an id, the script it belongs to as well as the index of a page.
 * <br>
 * This DTO is intended for the use of already stored pages.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private Long id;
    private Long scriptId;
    private Long index;
    private List<LineDto> lines;
}
