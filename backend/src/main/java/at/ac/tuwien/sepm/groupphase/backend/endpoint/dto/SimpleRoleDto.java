package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Color;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRoleDto {
    private String name;
    private Color color;
}
