package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {
    @Mapping(target = "name", source = "roleName")
    SimpleRoleDto stringToSimpleRoleDto(String roleName);

    @Mapping(target = "name", source = "roleNames")
    List<SimpleRoleDto> listOfStringToListOfSimpleRoleDto(List<String> roleNames);
}
