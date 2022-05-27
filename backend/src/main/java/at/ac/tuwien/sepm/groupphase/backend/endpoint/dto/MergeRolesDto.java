package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto for merging roles.
 *
 * @author Luke Nemeskeri
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeRolesDto {
    private List<Long> ids;
    private String newName;
}
