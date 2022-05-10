package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Staged Script Data Access Object.
 * <br>
 * This DAO only includes simple data access objects.
 * The roles are only saved as role names.
 * <br>
 * The staged script DTO is intended for newly uploaded and parsed
 * scripts, that are not yet stored.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SimpleScriptDto {
    private String name;
    private List<SimplePageDto> pages;
    private List<SimpleRoleDto> roles;
}
