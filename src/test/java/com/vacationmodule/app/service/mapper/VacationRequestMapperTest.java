package com.vacationmodule.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VacationRequestMapperTest {

    private VacationRequestMapper vacationRequestMapper;

    @BeforeEach
    public void setUp() {
        vacationRequestMapper = new VacationRequestMapperImpl();
    }
}
