package com.vacationmodule.app.service.mapper;

import com.vacationmodule.app.domain.Project;
import com.vacationmodule.app.domain.User;
import com.vacationmodule.app.service.dto.ProjectDTO;
import com.vacationmodule.app.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "projectManager", source = "projectManager", qualifiedByName = "userLogin")
    @Mapping(target = "techLead", source = "techLead", qualifiedByName = "userLogin")
    ProjectDTO toDto(Project s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
