package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = ColorMapper.class)
public interface RoleMapper {
    @Mapping(target = "name", source = "roleName")
    SimpleRoleDto stringToSimpleRoleDto(String roleName);

    @Mapping(target = "name", source = "roleNames")
    List<SimpleRoleDto> listOfStringToListOfSimpleRoleDto(List<String> roleNames);

    @Mapping(target = "scriptId", source = "script.id")
    RoleDto roleToRoleDto(Role role);

    List<RoleDto> setOfRoleToListOfRoleDto(Set<Role> roleSet);
}
