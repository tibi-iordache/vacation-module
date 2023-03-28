package com.vacationmodule.app.service.mapper;

import com.vacationmodule.app.domain.Project;
import com.vacationmodule.app.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
