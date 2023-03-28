package com.vacationmodule.app.service.mapper;

import com.vacationmodule.app.domain.User;
import com.vacationmodule.app.domain.VacationRequest;
import com.vacationmodule.app.service.dto.UserDTO;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VacationRequest} and its DTO {@link VacationRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface VacationRequestMapper extends EntityMapper<VacationRequestDTO, VacationRequest> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "approbedBies", source = "approbedBies", qualifiedByName = "userLoginSet")
    VacationRequestDTO toDto(VacationRequest s);

    VacationRequest toEntity(VacationRequestDTO vacationRequestDTO);

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
