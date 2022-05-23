package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Color;

/**
 * Simple Color Data Transfer Object.
 * <br>
 * This is used to abstract over an awt.Color Object which holds too much
 * information.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleColorDto {
    private int red;
    private int green;
    private int blue;

    public SimpleColorDto(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Color asColor() {
        return new Color(red, green, blue);
    }
}
