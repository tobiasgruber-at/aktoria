package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleColorDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.awt.Color;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ColorMapper {

    SimpleColorDto colorToSimpleColorDto(Color color);
}
