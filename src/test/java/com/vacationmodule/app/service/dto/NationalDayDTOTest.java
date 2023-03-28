package com.vacationmodule.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vacationmodule.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalDayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NationalDayDTO.class);
        NationalDayDTO nationalDayDTO1 = new NationalDayDTO();
        nationalDayDTO1.setId(1L);
        NationalDayDTO nationalDayDTO2 = new NationalDayDTO();
        assertThat(nationalDayDTO1).isNotEqualTo(nationalDayDTO2);
        nationalDayDTO2.setId(nationalDayDTO1.getId());
        assertThat(nationalDayDTO1).isEqualTo(nationalDayDTO2);
        nationalDayDTO2.setId(2L);
        assertThat(nationalDayDTO1).isNotEqualTo(nationalDayDTO2);
        nationalDayDTO1.setId(null);
        assertThat(nationalDayDTO1).isNotEqualTo(nationalDayDTO2);
    }
}
