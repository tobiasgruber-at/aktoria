package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "name", source = "roleName")
    SimpleRoleDto stringToSimpleRoleDto(String roleName);

    @Mapping(target = "name", source = "roleNames")
    List<SimpleRoleDto> listOfStringToListOfSimpleRoleDto(List<String> roleNames);
}
