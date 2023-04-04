package com.vacationmodule.app.service.mapper;

import com.vacationmodule.app.domain.Project;
import com.vacationmodule.app.domain.Team;
import com.vacationmodule.app.domain.User;
import com.vacationmodule.app.service.dto.ProjectDTO;
import com.vacationmodule.app.service.dto.TeamDTO;
import com.vacationmodule.app.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
    @Mapping(target = "users", source = "users", qualifiedByName = "userLoginSet")
    TeamDTO toDto(Team s);

    @Mapping(target = "removeUser", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userLoginSet")
    default Set<UserDTO> toDtoUserLoginSet(Set<User> user) {
        return user.stream().map(this::toDtoUserLogin).collect(Collectors.toSet());
    }
}
