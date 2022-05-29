package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Script Data Transfer Object.
 * <br>
 * In contrast to the simple script DTO this DTO is intended
 * for already stored scripts.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptDto {
    private Long id;
    private String name;
    private List<PageDto> pages;
    private List<RoleDto> roles;
    private SimpleUserDto owner;
    private List<SimpleUserDto> participants;
}
