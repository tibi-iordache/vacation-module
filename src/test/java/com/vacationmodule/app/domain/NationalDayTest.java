package com.vacationmodule.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vacationmodule.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NationalDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NationalDay.class);
        NationalDay nationalDay1 = new NationalDay();
        nationalDay1.setId(1L);
        NationalDay nationalDay2 = new NationalDay();
        nationalDay2.setId(nationalDay1.getId());
        assertThat(nationalDay1).isEqualTo(nationalDay2);
        nationalDay2.setId(2L);
        assertThat(nationalDay1).isNotEqualTo(nationalDay2);
        nationalDay1.setId(null);
        assertThat(nationalDay1).isNotEqualTo(nationalDay2);
    }
}
