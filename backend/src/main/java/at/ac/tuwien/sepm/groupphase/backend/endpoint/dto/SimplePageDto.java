package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simple Page Data Transfer Object.
 * <br>
 * A simple page DTO only contains the line that start on
 * this page.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePageDto {
    private List<SimpleLineDto> lines;
    private Long index;
}
