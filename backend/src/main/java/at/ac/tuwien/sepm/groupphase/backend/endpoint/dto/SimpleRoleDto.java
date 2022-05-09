package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.awt.Color;
import java.util.Objects;

public class SimpleRoleDto {
    private String name;
    private Color color;

    public SimpleRoleDto(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public SimpleRoleDto() {
        this(null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRoleDto that = (SimpleRoleDto) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
