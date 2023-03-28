package com.vacationmodule.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vacationmodule.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VacationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacationRequest.class);
        VacationRequest vacationRequest1 = new VacationRequest();
        vacationRequest1.setId(1L);
        VacationRequest vacationRequest2 = new VacationRequest();
        vacationRequest2.setId(vacationRequest1.getId());
        assertThat(vacationRequest1).isEqualTo(vacationRequest2);
        vacationRequest2.setId(2L);
        assertThat(vacationRequest1).isNotEqualTo(vacationRequest2);
        vacationRequest1.setId(null);
        assertThat(vacationRequest1).isNotEqualTo(vacationRequest2);
    }
}
