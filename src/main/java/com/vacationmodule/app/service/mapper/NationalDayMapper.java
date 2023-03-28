package com.vacationmodule.app.service.mapper;

import com.vacationmodule.app.domain.NationalDay;
import com.vacationmodule.app.service.dto.NationalDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NationalDay} and its DTO {@link NationalDayDTO}.
 */
@Mapper(componentModel = "spring")
public interface NationalDayMapper extends EntityMapper<NationalDayDTO, NationalDay> {}
