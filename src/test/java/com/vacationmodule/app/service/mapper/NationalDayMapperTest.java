package com.vacationmodule.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NationalDayMapperTest {

    private NationalDayMapper nationalDayMapper;

    @BeforeEach
    public void setUp() {
        nationalDayMapper = new NationalDayMapperImpl();
    }
}
